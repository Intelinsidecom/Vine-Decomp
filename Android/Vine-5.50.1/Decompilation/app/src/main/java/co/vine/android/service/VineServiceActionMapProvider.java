package co.vine.android.service;

import java.util.HashMap;

/* loaded from: classes.dex */
public final class VineServiceActionMapProvider {
    private final HashMap<Integer, VineServiceAction> mActionCodeMap;
    private final HashMap<String, VineServiceAction> mActionMap;

    private VineServiceActionMapProvider(HashMap<String, VineServiceAction> actionMap, HashMap<Integer, VineServiceAction> actionCodeMap) {
        this.mActionMap = actionMap;
        this.mActionCodeMap = actionCodeMap;
    }

    public VineServiceAction getAction(String actionString) {
        return this.mActionMap.get(actionString);
    }

    public VineServiceAction getAction(int actionCode) {
        return this.mActionCodeMap.get(Integer.valueOf(actionCode));
    }

    public static class Builder {
        private final HashMap<String, VineServiceAction> mStringActions = new HashMap<>(4);
        private final HashMap<Integer, VineServiceAction> mIntActions = new HashMap<>(100);
        private int mNextActionCode = 1;

        public void registerAsActionString(String actionString, VineServiceAction action) {
            if (actionString == null || action == null) {
                throw new IllegalArgumentException("Invalid action to register");
            }
            VineServiceAction previous = this.mStringActions.put(actionString, action);
            if (previous != null && previous != action) {
                throw new IllegalArgumentException("Action string " + actionString + " is already reigstered.");
            }
        }

        public int registerAsActionCode(VineServiceAction action) {
            int actionCodeToUse = this.mNextActionCode;
            this.mIntActions.put(Integer.valueOf(actionCodeToUse), action);
            this.mNextActionCode++;
            return actionCodeToUse;
        }

        public VineServiceActionMapProvider build() {
            return new VineServiceActionMapProvider(this.mStringActions, this.mIntActions);
        }
    }
}
