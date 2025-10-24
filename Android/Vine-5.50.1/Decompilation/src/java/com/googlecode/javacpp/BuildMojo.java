package com.googlecode.javacpp;

import java.io.File;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/* loaded from: classes.dex */
public class BuildMojo extends AbstractMojo {
    private String classPath = null;
    private String[] classPaths = null;
    private File outputDirectory = null;
    private String outputName = null;
    private boolean compile = true;
    private boolean header = false;
    private boolean copyLibs = false;
    private String jarPrefix = null;
    private String properties = null;
    private File propertyFile = null;
    private Properties propertyKeysAndValues = null;
    private String classOrPackageName = null;
    private String[] classOrPackageNames = null;
    private Map<String, String> environmentVariables = null;
    private String[] compilerOptions = null;
    private boolean skip = false;

    /* JADX INFO: Thrown type has an unknown type hierarchy: org.apache.maven.plugin.MojoExecutionException */
    public void execute() throws MojoExecutionException {
        try {
            getLog().info("Executing JavaCPP Builder");
            if (getLog().isDebugEnabled()) {
                getLog().debug("classPath: " + this.classPath);
                getLog().debug("classPaths: " + Arrays.deepToString(this.classPaths));
                getLog().debug("outputDirectory: " + this.outputDirectory);
                getLog().debug("outputName: " + this.outputName);
                getLog().debug("compile: " + this.compile);
                getLog().debug("header: " + this.header);
                getLog().debug("copyLibs: " + this.copyLibs);
                getLog().debug("jarPrefix: " + this.jarPrefix);
                getLog().debug("properties: " + this.properties);
                getLog().debug("propertyFile: " + this.propertyFile);
                getLog().debug("propertyKeysAndValues: " + this.propertyKeysAndValues);
                getLog().debug("classOrPackageName: " + this.classOrPackageName);
                getLog().debug("classOrPackageNames: " + Arrays.deepToString(this.classOrPackageNames));
                getLog().debug("environmentVariables: " + this.environmentVariables);
                getLog().debug("compilerOptions: " + Arrays.deepToString(this.compilerOptions));
                getLog().debug("skip: " + this.skip);
            }
            if (this.skip) {
                getLog().info("Skipped execution of JavaCPP Builder");
                return;
            }
            if (this.classPaths != null && this.classPath != null) {
                this.classPaths = (String[]) Arrays.copyOf(this.classPaths, this.classPaths.length + 1);
                this.classPaths[this.classPaths.length - 1] = this.classPath;
            } else if (this.classPath != null) {
                this.classPaths = new String[]{this.classPath};
            }
            if (this.classOrPackageNames != null && this.classOrPackageName != null) {
                this.classOrPackageNames = (String[]) Arrays.copyOf(this.classOrPackageNames, this.classOrPackageNames.length + 1);
                this.classOrPackageNames[this.classOrPackageNames.length - 1] = this.classOrPackageName;
            } else if (this.classOrPackageName != null) {
                this.classOrPackageNames = new String[]{this.classOrPackageName};
            }
            File[] outputFiles = new Builder().classPaths(this.classPaths).outputDirectory(this.outputDirectory).outputName(this.outputName).compile(this.compile).header(this.header).copyLibs(this.copyLibs).jarPrefix(this.jarPrefix).properties(this.properties).propertyFile(this.propertyFile).properties(this.propertyKeysAndValues).classesOrPackages(this.classOrPackageNames).environmentVariables(this.environmentVariables).compilerOptions(this.compilerOptions).build();
            getLog().info("Successfully executed JavaCPP Builder");
            if (getLog().isDebugEnabled()) {
                getLog().debug("outputFiles: " + Arrays.deepToString(outputFiles));
            }
        } catch (Exception e) {
            getLog().error("Failed to execute JavaCPP Builder: " + e.getMessage());
            throw new MojoExecutionException("Failed to execute JavaCPP Builder", e);
        }
    }
}
