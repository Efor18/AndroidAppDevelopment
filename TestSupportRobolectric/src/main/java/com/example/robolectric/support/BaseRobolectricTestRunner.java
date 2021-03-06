package com.example.robolectric.support;


import org.androidannotations.api.BackgroundExecutor;
import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.SdkConfig;
import org.robolectric.SdkEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.bytecode.ClassInfo;
import org.robolectric.bytecode.Setup;

import java.io.File;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Robolectric Support
 * <p/>
 * Robolectric supports org.robolectric.Config.properties but this project setup does not support
 * loading files from resources folder in android studio https://github.com/evant/android-studio-unit-test-plugin/issues/4
 * <p/>
 * So do here:
 * <p/>
 * <li>Set default shadows for all tests</li>*
 * <li>Set default emulated SDK version</li>
 * <li>Set path to the android manifest file</li>
 * <p/>
 * -
 */
public abstract class BaseRobolectricTestRunner extends RobolectricTestRunner {

    /**
     * Register which classes may be shadowed.
     * <p/>
     * Example implementation:
     * <p/>
     * <pre>
     * protected Class[] getClassesToShadow() {
     *     return new Class[0];
     *     return new Class[] {ClassToShadow.class};
     * }
     * </pre>
     *
     * @return empty collection or classes to shadow.
     */
    protected abstract Class[] getClassesToShadow();

    /**
     * Register default shadow classes.
     * <p/>
     * Example implementation:
     * <p/>
     * <pre>
     * protected Class[] getDefaultShadowClasses() {
     *     return new Class[0];
     *     return new Class[] {ShadowClass.class};
     * }
     * </pre>
     *
     * @return empty collection or shadow classes.
     */
    protected abstract Class[] getDefaultShadowClasses();

    private ArrayList<String> classesToShadow = new ArrayList<>();
    private String defaultShadowClasses = CustomShadowApplication.class.getName()
            + " " + ShadowBackgroundExecutor.class.getName();

    public BaseRobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);

        for (Class aClass : getClassesToShadow()) {
            classesToShadow.add(aClass.getName());
        }

        for (Class aClass : getDefaultShadowClasses()) {
            defaultShadowClasses += " " + aClass.getName();
        }

        // default exception handler for background threads don't report full stack traces
        // following code throws the exception with correct cause stack trace.
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, final Throwable ex) {
                RuntimeException runtimeException;
                runtimeException = new RuntimeException("Uncaught background task exception");
                runtimeException.initCause(ex);
                throw runtimeException;
            }
        });
    }

    @Override
    protected ClassLoader createRobolectricClassLoader(Setup setup, SdkConfig sdkConfig) {
        return super.createRobolectricClassLoader(new ExtraShadows(setup), sdkConfig);
    }

    @Override
    protected SdkConfig pickSdkVersion(AndroidManifest appManifest, Config config) {
        // current robolectric supports not the latest android sdk version
        // so we must downgrade to simulate the latest supported version.
        Config.Implementation implementation = overwriteConfig(config, "emulateSdk", "18");
        return super.pickSdkVersion(appManifest, implementation);
    }

    protected Config.Implementation overwriteConfig(Config config, String key, String value) {
        Properties properties = new Properties();
        properties.setProperty(key, value);
        return new Config.Implementation(config, Config.Implementation.fromProperties(properties));
    }

    protected AndroidManifest getAppManifest(Config config) {
        String path = "src/main/AndroidManifest.xml";

        // android studio has different execution root for tests than pure gradle
        // so we avoid here manual effort to get them running inside android studio
        if (!new File(path).exists()) {
            path = "Data/" + path;
        }

        Properties properties = new Properties();
        properties.setProperty("manifest", path);
        return super.getAppManifest(new Config.Implementation(config, Config.Implementation.fromProperties(properties)));
    }

    @Override
    protected void configureShadows(SdkEnvironment sdkEnvironment, Config config) {
        Properties properties = new Properties();
        properties.setProperty("shadows", defaultShadowClasses);
        super.configureShadows(sdkEnvironment, new Config.Implementation(config, Config.Implementation.fromProperties(properties)));
    }

    class ExtraShadows extends Setup {
        private Setup setup;

        public ExtraShadows(Setup setup) {
            this.setup = setup;
        }

        public boolean shouldInstrument(ClassInfo classInfo) {
            boolean shouldInstrument = setup.shouldInstrument(classInfo);
            return shouldInstrument
                    || classesToShadow.contains(classInfo.getName())
                    || classInfo.getName().equals(BackgroundExecutor.class.getName());
        }
    }
}
