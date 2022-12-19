package edu.umich.dpm.sensorgrabber;

import static junit.framework.TestCase.assertEquals;

import android.app.Application;
import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;

public class ExampleInstrumentTest {
//    public ExampleInstrumentTest() {
//        super(Application.class);
//    }

    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.geetaregmi.sensorgrabber", appContext.getPackageName());
    }
}