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
public class TVDBTests {
    @Test
    public void getToken() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);
        TheTVDB.getInstance(new TheTVDB.TokenListener() {
            @Override
            public void OnTokenReceived(String token) {
                assertNotNull(token);
                lock.countDown();
            }
        });
        lock.await(5000, TimeUnit.MILLISECONDS);
        assertNotNull(TheTVDB.token);
    }

    @Test
    public void testImageSummary() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);
        final String titan = "267440";
        TheTVDB.getInstance(new TheTVDB.TokenListener() {
            @Override
            public void OnTokenReceived(String token) {
                TheTVDB.getInstance(null).getImageSummary(titan, new TheTVDB.ImageSummaryListener() {
                    @Override
                    public void OnImageSummary(ImageSummary summary) {
                        assertNotNull(summary);
                        lock.countDown();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        assertTrue(false);
                    }
                });
            }
        });
        lock.await(5000, TimeUnit.MILLISECONDS);
    }

    @Test
    public void testBanners() throws Exception {
        final CountDownLatch lock = new CountDownLatch(1);
        //Attack on titan :D
        final String titan = "267440";
        TheTVDB.getInstance(new TheTVDB.TokenListener() {
            @Override
            public void OnTokenReceived(String token) {
                TheTVDB.getInstance(null).getBanners(titan, new TheTVDB.BannerListener() {
                    @Override
                    public void OnBanner(List<String> images) {
                        lock.countDown();
                        //assertNotNull(images);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.err.print(error.getMessage());
                        //If this errors out then the test should just fail
                        assertTrue(false);
                    }
                });
            }
        });
        lock.await(5000, TimeUnit.MILLISECONDS);
    }
}
