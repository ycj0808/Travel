package com.android.travel.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * ImageUtils
 * <ul>
 * convert between Bitmap, byte array, Drawable
 * <li>{@link #bitmapToByte(Bitmap)}</li>
 * <li>{@link #bitmapToDrawable(Bitmap)}</li>
 * <li>{@link #byteToBitmap(byte[])}</li>
 * <li>{@link #byteToDrawable(byte[])}</li>
 * <li>{@link #drawableToBitmap(Drawable)}</li>
 * <li>{@link #drawableToByte(Drawable)}</li>
 * </ul>
 * <ul>
 * get image
 * <li>{@link #getInputStreamFromUrl(String, int)}</li>
 * <li>{@link #getBitmapFromUrl(String, int)}</li>
 * <li>{@link #getDrawableFromUrl(String, int)}</li>
 * </ul>
 * <ul>
 * scale image
 * <li>{@link #scaleImageTo(Bitmap, int, int)}</li>
 * <li>{@link #scaleImage(Bitmap, float, float)}</li>
 * </ul>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2012-6-27
 */
public class ImageUtils {

	/**
	 * convert Bitmap to byte array
	 * 
	 * @param b
	 * @return
	 */
	public static byte[] bitmapToByte(Bitmap b) {
		if (b == null) {
			return null;
		}

		ByteArrayOutputStream o = new ByteArrayOutputStream();
		b.compress(Bitmap.CompressFormat.PNG, 100, o);
		return o.toByteArray();
	}

	/**
	 * convert byte array to Bitmap
	 * 
	 * @param b
	 * @return
	 */
	public static Bitmap byteToBitmap(byte[] b) {
		return (b == null || b.length == 0) ? null : BitmapFactory
				.decodeByteArray(b, 0, b.length);
	}

	/**
	 * convert Drawable to Bitmap
	 * 
	 * @param d
	 * @return
	 */
	public static Bitmap drawableToBitmap(Drawable d) {
		return d == null ? null : ((BitmapDrawable) d).getBitmap();
	}

	/**
	 * convert Bitmap to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable bitmapToDrawable(Bitmap b) {
		return b == null ? null : new BitmapDrawable(b);
	}

	/**
	 * convert Drawable to byte array
	 * 
	 * @param d
	 * @return
	 */
	public static byte[] drawableToByte(Drawable d) {
		return bitmapToByte(drawableToBitmap(d));
	}

	/**
	 * convert byte array to Drawable
	 * 
	 * @param b
	 * @return
	 */
	public static Drawable byteToDrawable(byte[] b) {
		return bitmapToDrawable(byteToBitmap(b));
	}

	


	
	/**
	 * scale image
	 * 
	 * @param org
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap scaleImageTo(Bitmap org, int newWidth, int newHeight) {
		return scaleImage(org, (float) newWidth / org.getWidth(),
				(float) newHeight / org.getHeight());
	}

	/**
	 * scale image
	 * 
	 * @param org
	 * @param scaleWidth
	 *            sacle of width
	 * @param scaleHeight
	 *            scale of height
	 * @return
	 */
	public static Bitmap scaleImage(Bitmap org, float scaleWidth,
			float scaleHeight) {
		if (org == null) {
			return null;
		}

		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		return Bitmap.createBitmap(org, 0, 0, org.getWidth(), org.getHeight(),
				matrix, true);
	}

	/**
	 * close inputStream
	 * 
	 * @param s
	 */
	private static void closeInputStream(InputStream s) {
		if (s == null) {
			return;
		}

		try {
			s.close();
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		}
	}

	/**
	 * 提取图像Alpha
	 * 
	 * @param mBitmap
	 * @param mColor
	 * @return
	 */
	public static Bitmap getAlphaBitmap(Bitmap mBitmap, int mColor) {
		Bitmap mAlphaBitmap = null;
		if (mBitmap != null) {
			mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
					mBitmap.getHeight(), Config.ARGB_8888);
			Canvas mCanvas = new Canvas(mAlphaBitmap);
			Paint mPaint = new Paint();
			mPaint.setColor(mColor);
			// 从原位图中提取只包含alpha的位图
			Bitmap alphaBitmap = mBitmap.extractAlpha();
			// 在画布上（mAlphaBitmap）绘制alpha位图
			mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		}
		return mAlphaBitmap;
	}

	/**
	 * 设置背景图片
	 * 
	 * @param view
	 * @param mBitmap
	 * @param mColor
	 */
	@SuppressWarnings("deprecation")
	public static void setbackground(View view, Bitmap mBitmap, int mColor) {
		Bitmap mAlphaBitmap = Bitmap.createBitmap(mBitmap.getWidth(),
				mBitmap.getHeight(), Config.ARGB_8888);
		Canvas mCanvas = new Canvas(mAlphaBitmap);
		Paint mPaint = new Paint();
		mPaint.setColor(mColor);
		// 从原位图中提取只包含alpha的位图
		Bitmap alphaBitmap = mBitmap.extractAlpha();
		// 在画布上（mAlphaBitmap）绘制alpha位图
		mCanvas.drawBitmap(alphaBitmap, 0, 0, mPaint);
		view.setBackgroundDrawable(ImageUtils.bitmapToDrawable(mAlphaBitmap));
	}

	public static void setImageBg(ImageView view, Bitmap mBitmap, int mColor) {
		Bitmap bitmap = getAlphaBitmap(mBitmap, mColor);
		view.setImageBitmap(bitmap);
	}

	/**
	 * 设置图片自适应
	 * 
	 * @param view
	 * @param activity
	 * @param drawable
	 */
	public static void setAdaptaImage(View view, Activity activity, int drawable) {
		Bitmap bm = BitmapFactory.decodeResource(activity.getResources(),
				drawable);
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int tmpWidth = bm.getWidth();
		int tmpHeight = bm.getHeight();
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
				.getLayoutParams();
		params.height = (int) (tmpHeight * ((double) dm.widthPixels / (double) tmpWidth));
		view.setLayoutParams(params);
	}

	/**
	 * 获取特定Bitmap
	 * 
	 * @param res
	 * @param drawable
	 * @return
	 */
	public static Bitmap getCustomBitmap(Resources res, int drawable) {
		return BitmapFactory.decodeResource(res, drawable);
	}

	/**
	 * 设置图片背景色
	 * 
	 * @param view
	 * @param res
	 * @param drawable
	 * @param mColor
	 */
	public static void setImageBg(ImageView view, Resources res, int drawable,
			int mColor) {
		view.setImageBitmap(getAlphaBitmap(getCustomBitmap(res, drawable),
				mColor));
	}
	/**
	 * 设置图片的背景色
	 * @param view
	 * @param drawable
	 * @param mColor
	 */
	public static void setImageBg(ImageView view,int mColor) {
		view.setImageBitmap(getAlphaBitmap(
				ImageUtils.drawableToBitmap(view.getDrawable()), mColor));
	}
	
	/**
	 * 设置View背景
	 * @param context
	 * @param idNormal
	 * @param normalColor
	 * @param selectColor
	 * @return
	 */
	public static StateListDrawable newSelector(Context context, int idNormal,
			int normalColor, int selectColor) {
		StateListDrawable bg = new StateListDrawable();
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),idNormal);
		Bitmap mNormalBitmap = getAlphaBitmap(bitmap, normalColor);
		Bitmap mPressedBitmap = getAlphaBitmap(bitmap, selectColor);
		Drawable normalDrawable = bitmapToDrawable(mNormalBitmap);
		Drawable pressedDrawable = bitmapToDrawable(mPressedBitmap);
		bg.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressedDrawable);
		bg.addState(new int[] { android.R.attr.state_enabled,
				android.R.attr.state_focused }, pressedDrawable);
		bg.addState(new int[] { android.R.attr.state_enabled }, normalDrawable);
		bg.addState(new int[] { android.R.attr.state_focused }, pressedDrawable);
		return bg;
	}
	
	 public static StateListDrawable newSelector(Context context, int idNormal,int normalColor,int selectColor,int unableColor){
		 StateListDrawable bg = new StateListDrawable();
		 Bitmap bitmap= BitmapFactory.decodeResource(context.getResources(), idNormal);
		 Bitmap mNormalBitmap =getAlphaBitmap(bitmap, normalColor);
		 Bitmap mPressedBitmap=getAlphaBitmap(bitmap, selectColor);
		 Bitmap mUnableBitmap=null;
		 if(unableColor!=-1){
			 mUnableBitmap=getAlphaBitmap(bitmap, unableColor);
		 }
		 Drawable normalDrawable=bitmapToDrawable(mNormalBitmap);
		 Drawable pressedDrawable=bitmapToDrawable(mPressedBitmap);
		 Drawable unableDrawable=bitmapToDrawable(mUnableBitmap);
		 bg.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled}, pressedDrawable);
		 bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, pressedDrawable);
		 bg.addState(new int[] { android.R.attr.state_enabled }, normalDrawable);
		 bg.addState(new int[] { android.R.attr.state_focused }, pressedDrawable);
		 
		 if(unableDrawable!=null){
			 bg.addState(new int[] { android.R.attr.state_window_focused }, unableDrawable);
		 }
		 bg.addState(new int[] {}, normalDrawable);
		 return bg;
	 }
	
	 /**
	  * 设置View背景变化
	  * @param normalDrawable
	  * @param pressedDrawable
	  * @return
	  */
	 public static StateListDrawable newSelector(Drawable normalDrawable,Drawable pressedDrawable){
		 StateListDrawable bg = new StateListDrawable();
		 bg.addState(new int[]{android.R.attr.state_pressed,android.R.attr.state_enabled}, pressedDrawable);
		 bg.addState(new int[] { android.R.attr.state_enabled, android.R.attr.state_focused }, pressedDrawable);
		 bg.addState(new int[] { android.R.attr.state_enabled }, normalDrawable);
		 bg.addState(new int[] { android.R.attr.state_focused }, pressedDrawable);
		 return bg;
	 }
	/**
	 * 文字颜色
	 * @param normalColor
	 * @param pressColor
	 * @param unableColor
	 * @return
	 */
	public static ColorStateList createColorState(int normalColor,int pressColor,int unableColor){
		 int[] colors = new int[] { pressColor, pressColor, normalColor, pressColor, unableColor, normalColor };
		 int[][] states=new int[6][];
		 states[0] = new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled };
	     states[1] = new int[] { android.R.attr.state_enabled, android.R.attr.state_focused };
	     states[2] = new int[] { android.R.attr.state_enabled };
	     states[3] = new int[] { android.R.attr.state_focused };
	     states[4] = new int[] { android.R.attr.state_window_focused };
	     states[5] = new int[] {};
	     ColorStateList colorList=new ColorStateList(states,colors);
	     return colorList;
	}
	
	/**
	 * 创建LayerDrawable
	 * @param lay0
	 * @param lay1
	 * @return
	 */
	public static LayerDrawable createLayerDrawable(Drawable lay0,Drawable lay1){
		Drawable [] layers=new Drawable[2];
		layers[0]=lay0;
		layers[1]=lay1;
		LayerDrawable layerDrawable=new LayerDrawable(layers);
		return layerDrawable;
	}
}
