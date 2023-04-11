package com.cbm.android.corneringlayout

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import java.util.regex.Pattern

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.cbm.android.corneringlayout", appContext.packageName)
    }

    @Test
    fun checkRegex() {
        val itA = "12345"
        val itB = "aBCdE"
        val itC = "123"
        val itD = "123ABC"
        val itE = "ABC123"
        val itF = "a2c4D4G5"
        val itG = "!\"#\$%&'()*+,-./:;<=>?@[\\]^_`{|}~"//"".*&!@#$%^&*()"
        val itH = "a2#"
        val itJ = "a2c4D4G5\".*&!@#\$%^&*()"
//        assert(Pattern.matches("\\d+", itA))
//        assertFalse(!Pattern.matches("\\d+", itA!!)&&itA!!.length<5)
//        assertFalse(Pattern.matches("[\\d]+", itB))
//        assert(Pattern.matches("\\d+", itA!!)&&itA!!.length>=5)
//        assert(Pattern.matches("\\d+[a-zA-Z]+", itD!!))
//        assert(Pattern.matches("[a-zA-Z]+\\d+", itE!!))
//        assert(Pattern.matches("(?=(.*[0-9])+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))", itF))
//        assert(Pattern.matches("[\\d+[a-zA-Z]\\W]+", itG!!))
//        assertFalse(Pattern.matches("[\\d+[a-zA-Z]]+", itH!!))
//        //assert(Pattern.matches("[\\d+[a-zA-Z]\\p{Punct}]+", itG!!))
//        assert(Pattern.matches(/*"[\\p{Punct}]+"*/"[[^\\d]+&&[^a-zA-Z]+]+", itG))
//        assertFalse(Pattern.matches("[\\W]+", itF))
        assertFalse(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itA).find())
        assertFalse(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itB).find())
        assertFalse(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itD).find())
        assertFalse(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itE).find())
        assertFalse(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itF).find())
        assertFalse(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itG).find())
        assert(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itH).find())
        assert(Pattern.compile("^(?=(.*\\d)+)((?=(.*[a-z])+)|(?=(.*[A-Z])+))(?=(.*[^\\da-zA-Z])+)").matcher(itJ).find())
    }

    @Test
    fun checkMath() {
        assert((2.0).toInt()>0)
        assert((2.4).toInt()>0)
        assert((2.8).toInt()>0)
    }
}