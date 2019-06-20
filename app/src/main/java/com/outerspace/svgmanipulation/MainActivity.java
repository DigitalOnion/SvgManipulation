package com.outerspace.svgmanipulation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatImageView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    CaptionImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_main);

        Drawable drawable = AppCompatResources.getDrawable(this, R.drawable.ic_buttonscontrol_controller_no_lines);

        image = findViewById(R.id.test_image);

        CaptionImageView.CaptionDefinition caption;
        image.resetCaptions();
        caption = new CaptionImageView.CaptionDefinition()
                .setPointer(0.0F, -0.24F)
                .moveBy(0.0F, -0.2F)
                .moveBy(-0.1F, -0.0F)
                .setCaptionText("Luis is cool!")
                .setCaptionDirection(CaptionImageView.CaptionDirection.LEFT)
                .setPointerVisible(false);
        image.addCaption(caption);
        caption = new CaptionImageView.CaptionDefinition()
                .setPointer(0.0F, -0.24F)
                .moveBy(0.0F, -0.2F)
                .moveBy(0.1F, -0.0F)
                .setCaptionText("Luis is really cool!")
                .setCaptionDirection(CaptionImageView.CaptionDirection.RIGHT)
                .setPointerVisible(false);
        image.addCaption(caption);
        caption = new CaptionImageView.CaptionDefinition()
                .setPointer(0.0F, -0.24F)
                .moveBy(0.25F, -0.0F)
                .moveBy(0.0F, -0.1F)
                .setCaptionText("¡Hola al mundo!")
                .setCaptionDirection(CaptionImageView.CaptionDirection.ABOVE)
                .setPointerVisible(false);
        image.addCaption(caption);
        caption = new CaptionImageView.CaptionDefinition()
                .setPointer(0.0F, -0.24F)
                .moveBy(0.25F, -0.0F)
                .moveBy(0.0F, 0.1F)
                .setCaptionText("Bonjour le monde!")
                .setCaptionDirection(CaptionImageView.CaptionDirection.BELOW)
                .setPointerVisible(false);
        image.addCaption(caption);
        caption = new CaptionImageView.CaptionDefinition()
                .setPointer(0.0F, -0.24F)
                .moveBy(-0.22F, 0.3F)
                .setTextWidth(0.20f)
                .setCaptionText("Magic Leap is the coolest technology ever!")
                .setCaptionDirection(CaptionImageView.CaptionDirection.BELOW)
                .setPointerVisible(false);
        image.addCaption(caption);


        image.setImageDrawable(drawable);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
