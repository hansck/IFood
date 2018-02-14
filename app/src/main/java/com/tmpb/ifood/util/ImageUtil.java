package com.tmpb.ifood.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.tmpb.ifood.R;

import java.io.FileDescriptor;


public class ImageUtil {

	private static ImageUtil instance = new ImageUtil();

	public static ImageUtil getInstance() {
		return instance;
	}

	private ImageUtil() {
	}

	public void setImageResource(Context context, String url, ImageView view) {
		Glide.with(context)
			.load(url)
			.placeholder(R.drawable.ic_default_image)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.into(view);
	}

	public void setImageProfile(Context context, String url, ImageView view) {
		Glide.with(context)
			.load(url)
			.placeholder(R.drawable.ic_default_image)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.dontAnimate()
			.into(view);
	}

	public void setImageDrawable(Context context, int image, ImageView view) {
		Glide.with(context)
			.load(image)
			.diskCacheStrategy(DiskCacheStrategy.ALL)
			.into(view);
	}

	public Bitmap getResizedBitmap(Context context, Uri uri, int maxSize) {
		ParcelFileDescriptor parcel;
		try {
			parcel = context.getContentResolver().openFileDescriptor(uri, "r");
			FileDescriptor descriptor = parcel.getFileDescriptor();
			Bitmap image = BitmapFactory.decodeFileDescriptor(descriptor);
			parcel.close();
			float ratio = Math.min((float) maxSize / image.getWidth(), (float) maxSize / image.getHeight());
			if (ratio < 1) {
				int width = Math.round(ratio * image.getWidth());
				int height = Math.round(ratio * image.getHeight());
				return Bitmap.createScaledBitmap(image, width, height, true);
			} else {
				return image;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
