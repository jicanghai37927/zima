
package com.haiyunshan.zima.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 软键盘工具类
 *
 */
public class SoftInputUtils {

    /**
     * 显示软键盘
     * 
     * @param context
     * @param view
     */
    public static final void show(final Context context, final EditText view) {
        if (view == null) {
            return;
        }

        view.requestFocus();
        
        view.post(new Runnable() {
			@Override
			public void run() {
				InputMethodManager manager = (InputMethodManager)(context.getSystemService(Context.INPUT_METHOD_SERVICE));
				manager.showSoftInput(view, 0);
			}
		});
    }

    /**
     *
     * @param context
     * @param view
     */
    public static final void show(final Context context, final View view) {
        if (view == null) {
            return;
        }

        view.requestFocus();

        view.post(new Runnable() {
            @Override
            public void run() {
                InputMethodManager manager = (InputMethodManager)(context.getSystemService(Context.INPUT_METHOD_SERVICE));
                manager.showSoftInput(view, 0);
            }
        });
    }

    /**
     * 隐藏软键盘
     * 
     * @param context
     * @param view
     */
    public static final void hide(Context context, EditText view) {
        if (view == null) {
            return;
        }

        InputMethodManager manager = (InputMethodManager)(context.getSystemService(Context.INPUT_METHOD_SERVICE));
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     *
     * @param context
     * @param view
     */
    public static final void hide(Context context, View view) {
        if (view == null) {
            return;
        }

        InputMethodManager manager = (InputMethodManager)(context.getSystemService(Context.INPUT_METHOD_SERVICE));
        manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
