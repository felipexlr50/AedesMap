package com.example.felipe.aedesmap;


import android.test.ActivityInstrumentationTestCase2;

import android.test.suitebuilder.annotation.SmallTest;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class MenuActivityTest extends ActivityInstrumentationTestCase2<MenuActivity> {

    MenuActivity activity;

    public MenuActivityTest() {
        super(MenuActivity.class);
    }

    @Override
    protected void setUp() throws Exception{
        super.setUp();
        activity = getActivity();
    }

    @SmallTest
    public void testTextViewNotNull(){
        TextView textView = (TextView) activity.findViewById(R.id.tfLatLng);
        assertNotNull(textView);
    }

    @SmallTest
    public void testImageButtonNotNull(){
        ImageButton imageButton = (ImageButton) activity.findViewById(R.id.imageButton);
        assertNotNull(imageButton);
    }

    @SmallTest
    public void testLatValues(){
        assertTrue(activity.getLat()<90||activity.getLat()>-90);
    }

    @SmallTest
    public void testLngValues(){
        assertTrue(activity.getLng()<180||activity.getLng()>-180);

    }
}