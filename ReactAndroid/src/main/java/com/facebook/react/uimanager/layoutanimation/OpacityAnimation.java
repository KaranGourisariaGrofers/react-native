/*
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * This source code is licensed under the MIT license found in the
 * LICENSE file in the root directory of this source tree.
 */

package com.facebook.react.uimanager.layoutanimation;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Animation responsible for updating opacity of a view. It should ideally use hardware texture to
 * optimize rendering performances.
 */
/* package */ class OpacityAnimation extends Animation {

  static class OpacityAnimationListener implements Animation.AnimationListener {

    private final View mView;
    private boolean mLayerTypeChanged = false;

    public OpacityAnimationListener(View view) {
      mView = view;
    }

    @Override
    public void onAnimationStart(Animation animation) {
      if (mView.hasOverlappingRendering() && mView.getLayerType() == View.LAYER_TYPE_NONE) {
        mLayerTypeChanged = true;
        mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
      }
    }

    @Override
    public void onAnimationEnd(Animation animation) {
      if (mLayerTypeChanged) {
        mView.setLayerType(View.LAYER_TYPE_NONE, null);
      }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
      // do nothing
    }
  }

  private final View mView;
  private final float mCurrentOpacity;

  public OpacityAnimation(View view, float currentOpacity) {
    mView = view;
    mCurrentOpacity = currentOpacity;

    setAnimationListener(new OpacityAnimationListener(view));
  }

  @Override
  protected void applyTransformation(float interpolatedTime, Transformation t) {
    mView.setAlpha(mCurrentOpacity);
  }

  @Override
  public boolean willChangeBounds() {
    return false;
  }
}
