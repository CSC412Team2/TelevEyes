package edu.ecsu.csc412.televeyes;

import android.support.test.runner.AndroidJUnit4;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.ecsu.csc412.televeyes.json.thetvdb.Image;
import edu.ecsu.csc412.televeyes.json.thetvdb.ImageSummary;
import edu.ecsu.csc412.televeyes.tv.TheTVDB;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class HerokuTests {
    @Test
    public void testCategories(){

    }
}
