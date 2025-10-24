package android.support.v8.renderscript;

import android.support.v8.renderscript.Script;
import android.support.v8.renderscript.ScriptGroupThunker;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class ScriptGroup extends BaseObj {
    IO[] mInputs;
    IO[] mOutputs;

    static class IO {
        Allocation mAllocation;
        Script.KernelID mKID;

        IO(Script.KernelID s) {
            this.mKID = s;
        }
    }

    static class ConnectLine {
        Type mAllocationType;
        Script.KernelID mFrom;
        Script.FieldID mToF;
        Script.KernelID mToK;

        ConnectLine(Type t, Script.KernelID from, Script.KernelID to) {
            this.mFrom = from;
            this.mToK = to;
            this.mAllocationType = t;
        }

        ConnectLine(Type t, Script.KernelID from, Script.FieldID to) {
            this.mFrom = from;
            this.mToF = to;
            this.mAllocationType = t;
        }
    }

    static class Node {
        int dagNumber;
        Node mNext;
        Script mScript;
        ArrayList<Script.KernelID> mKernels = new ArrayList<>();
        ArrayList<ConnectLine> mInputs = new ArrayList<>();
        ArrayList<ConnectLine> mOutputs = new ArrayList<>();

        Node(Script s) {
            this.mScript = s;
        }
    }

    ScriptGroup(int id, RenderScript rs) {
        super(id, rs);
    }

    public void setInput(Script.KernelID s, Allocation a) {
        for (int ct = 0; ct < this.mInputs.length; ct++) {
            if (this.mInputs[ct].mKID == s) {
                this.mInputs[ct].mAllocation = a;
                this.mRS.nScriptGroupSetInput(getID(this.mRS), s.getID(this.mRS), this.mRS.safeID(a));
                return;
            }
        }
        throw new RSIllegalArgumentException("Script not found");
    }

    public void setOutput(Script.KernelID s, Allocation a) {
        for (int ct = 0; ct < this.mOutputs.length; ct++) {
            if (this.mOutputs[ct].mKID == s) {
                this.mOutputs[ct].mAllocation = a;
                this.mRS.nScriptGroupSetOutput(getID(this.mRS), s.getID(this.mRS), this.mRS.safeID(a));
                return;
            }
        }
        throw new RSIllegalArgumentException("Script not found");
    }

    public void execute() {
        this.mRS.nScriptGroupExecute(getID(this.mRS));
    }

    public static final class Builder {
        private int mKernelCount;
        private RenderScript mRS;
        private ScriptGroupThunker.Builder mT;
        private ArrayList<Node> mNodes = new ArrayList<>();
        private ArrayList<ConnectLine> mLines = new ArrayList<>();

        public Builder(RenderScript rs) {
            if (RenderScript.isNative) {
                this.mT = new ScriptGroupThunker.Builder(rs);
            }
            this.mRS = rs;
        }

        private void validateCycle(Node target, Node original) {
            for (int ct = 0; ct < target.mOutputs.size(); ct++) {
                ConnectLine cl = target.mOutputs.get(ct);
                if (cl.mToK != null) {
                    Node tn = findNode(cl.mToK.mScript);
                    if (tn.equals(original)) {
                        throw new RSInvalidStateException("Loops in group not allowed.");
                    }
                    validateCycle(tn, original);
                }
                if (cl.mToF != null) {
                    Node tn2 = findNode(cl.mToF.mScript);
                    if (tn2.equals(original)) {
                        throw new RSInvalidStateException("Loops in group not allowed.");
                    }
                    validateCycle(tn2, original);
                }
            }
        }

        private void mergeDAGs(int valueUsed, int valueKilled) {
            for (int ct = 0; ct < this.mNodes.size(); ct++) {
                if (this.mNodes.get(ct).dagNumber == valueKilled) {
                    this.mNodes.get(ct).dagNumber = valueUsed;
                }
            }
        }

        private void validateDAGRecurse(Node n, int dagNumber) {
            if (n.dagNumber != 0 && n.dagNumber != dagNumber) {
                mergeDAGs(n.dagNumber, dagNumber);
                return;
            }
            n.dagNumber = dagNumber;
            for (int ct = 0; ct < n.mOutputs.size(); ct++) {
                ConnectLine cl = n.mOutputs.get(ct);
                if (cl.mToK != null) {
                    Node tn = findNode(cl.mToK.mScript);
                    validateDAGRecurse(tn, dagNumber);
                }
                if (cl.mToF != null) {
                    Node tn2 = findNode(cl.mToF.mScript);
                    validateDAGRecurse(tn2, dagNumber);
                }
            }
        }

        private void validateDAG() {
            for (int ct = 0; ct < this.mNodes.size(); ct++) {
                Node n = this.mNodes.get(ct);
                if (n.mInputs.size() == 0) {
                    if (n.mOutputs.size() == 0 && this.mNodes.size() > 1) {
                        throw new RSInvalidStateException("Groups cannot contain unconnected scripts");
                    }
                    validateDAGRecurse(n, ct + 1);
                }
            }
            int dagNumber = this.mNodes.get(0).dagNumber;
            for (int ct2 = 0; ct2 < this.mNodes.size(); ct2++) {
                if (this.mNodes.get(ct2).dagNumber != dagNumber) {
                    throw new RSInvalidStateException("Multiple DAGs in group not allowed.");
                }
            }
        }

        private Node findNode(Script s) {
            for (int ct = 0; ct < this.mNodes.size(); ct++) {
                if (s == this.mNodes.get(ct).mScript) {
                    return this.mNodes.get(ct);
                }
            }
            return null;
        }

        private Node findNode(Script.KernelID k) {
            for (int ct = 0; ct < this.mNodes.size(); ct++) {
                Node n = this.mNodes.get(ct);
                for (int ct2 = 0; ct2 < n.mKernels.size(); ct2++) {
                    if (k == n.mKernels.get(ct2)) {
                        return n;
                    }
                }
            }
            return null;
        }

        public Builder addKernel(Script.KernelID k) {
            if (this.mT != null) {
                this.mT.addKernel(k);
            } else {
                if (this.mLines.size() != 0) {
                    throw new RSInvalidStateException("Kernels may not be added once connections exist.");
                }
                if (findNode(k) == null) {
                    this.mKernelCount++;
                    Node n = findNode(k.mScript);
                    if (n == null) {
                        n = new Node(k.mScript);
                        this.mNodes.add(n);
                    }
                    n.mKernels.add(k);
                }
            }
            return this;
        }

        public Builder addConnection(Type t, Script.KernelID from, Script.FieldID to) {
            if (this.mT != null) {
                this.mT.addConnection(t, from, to);
            } else {
                Node nf = findNode(from);
                if (nf == null) {
                    throw new RSInvalidStateException("From script not found.");
                }
                Node nt = findNode(to.mScript);
                if (nt == null) {
                    throw new RSInvalidStateException("To script not found.");
                }
                ConnectLine cl = new ConnectLine(t, from, to);
                this.mLines.add(new ConnectLine(t, from, to));
                nf.mOutputs.add(cl);
                nt.mInputs.add(cl);
                validateCycle(nf, nf);
            }
            return this;
        }

        public Builder addConnection(Type t, Script.KernelID from, Script.KernelID to) {
            if (this.mT != null) {
                this.mT.addConnection(t, from, to);
            } else {
                Node nf = findNode(from);
                if (nf == null) {
                    throw new RSInvalidStateException("From script not found.");
                }
                Node nt = findNode(to);
                if (nt == null) {
                    throw new RSInvalidStateException("To script not found.");
                }
                ConnectLine cl = new ConnectLine(t, from, to);
                this.mLines.add(new ConnectLine(t, from, to));
                nf.mOutputs.add(cl);
                nt.mInputs.add(cl);
                validateCycle(nf, nf);
            }
            return this;
        }

        public ScriptGroup create() {
            if (this.mT != null) {
                return this.mT.create();
            }
            if (this.mNodes.size() == 0) {
                throw new RSInvalidStateException("Empty script groups are not allowed");
            }
            for (int ct = 0; ct < this.mNodes.size(); ct++) {
                this.mNodes.get(ct).dagNumber = 0;
            }
            validateDAG();
            ArrayList<IO> inputs = new ArrayList<>();
            ArrayList<IO> outputs = new ArrayList<>();
            int[] kernels = new int[this.mKernelCount];
            int idx = 0;
            for (int ct2 = 0; ct2 < this.mNodes.size(); ct2++) {
                Node n = this.mNodes.get(ct2);
                int ct22 = 0;
                while (ct22 < n.mKernels.size()) {
                    Script.KernelID kid = n.mKernels.get(ct22);
                    int idx2 = idx + 1;
                    kernels[idx] = kid.getID(this.mRS);
                    boolean hasInput = false;
                    boolean hasOutput = false;
                    for (int ct3 = 0; ct3 < n.mInputs.size(); ct3++) {
                        if (n.mInputs.get(ct3).mToK == kid) {
                            hasInput = true;
                        }
                    }
                    for (int ct32 = 0; ct32 < n.mOutputs.size(); ct32++) {
                        if (n.mOutputs.get(ct32).mFrom == kid) {
                            hasOutput = true;
                        }
                    }
                    if (!hasInput) {
                        inputs.add(new IO(kid));
                    }
                    if (!hasOutput) {
                        outputs.add(new IO(kid));
                    }
                    ct22++;
                    idx = idx2;
                }
            }
            if (idx != this.mKernelCount) {
                throw new RSRuntimeException("Count mismatch, should not happen.");
            }
            int[] src = new int[this.mLines.size()];
            int[] dstk = new int[this.mLines.size()];
            int[] dstf = new int[this.mLines.size()];
            int[] types = new int[this.mLines.size()];
            for (int ct4 = 0; ct4 < this.mLines.size(); ct4++) {
                ConnectLine cl = this.mLines.get(ct4);
                src[ct4] = cl.mFrom.getID(this.mRS);
                if (cl.mToK != null) {
                    dstk[ct4] = cl.mToK.getID(this.mRS);
                }
                if (cl.mToF != null) {
                    dstf[ct4] = cl.mToF.getID(this.mRS);
                }
                types[ct4] = cl.mAllocationType.getID(this.mRS);
            }
            int id = this.mRS.nScriptGroupCreate(kernels, src, dstk, dstf, types);
            if (id == 0) {
                throw new RSRuntimeException("Object creation error, should not happen.");
            }
            ScriptGroup sg = new ScriptGroup(id, this.mRS);
            sg.mOutputs = new IO[outputs.size()];
            for (int ct5 = 0; ct5 < outputs.size(); ct5++) {
                sg.mOutputs[ct5] = outputs.get(ct5);
            }
            sg.mInputs = new IO[inputs.size()];
            for (int ct6 = 0; ct6 < inputs.size(); ct6++) {
                sg.mInputs[ct6] = inputs.get(ct6);
            }
            return sg;
        }
    }
}
