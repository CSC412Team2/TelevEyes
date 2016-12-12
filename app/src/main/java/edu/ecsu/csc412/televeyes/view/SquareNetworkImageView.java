
/*
 * Copyright (C) 2015 Antonio Leiva
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.ecu.csc412.televeyes.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SquareNetworkImageView  extends NetworkImageView {
    private List<OnBitmapSetListener> mBitmapListeners = new ArrayList<>();

    public SquareNetworkImageView(Context context) {
        super(context);
    }

    public SquareNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareNetworkImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        if(bm==null) return;

        for(int i = 0; i < mBitmapListeners.size(); i++){
            mBitmapListeners.get(i).OnBitmapSet(bm);
        }
        super.setImageBitmap(bm);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = getMeasuredWidth();
        setMeasuredDimension(width, width);
    }

    public void addBitmapListener(OnBitmapSetListener listener){
        if(listener != null)
        mBitmapListeners.add(listener);
    }

    public void removeBitmapListener(OnBitmapSetListener listener){
        mBitmapListeners.remove(listener);
    }

    public interface OnBitmapSetListener {
        void OnBitmapSet(Bitmap bm);
    }
}