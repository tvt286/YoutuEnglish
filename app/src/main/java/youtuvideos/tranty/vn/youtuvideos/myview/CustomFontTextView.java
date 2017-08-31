package youtuvideos.tranty.vn.youtuvideos.myview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import youtuvideos.tranty.vn.youtuvideos.R;
import youtuvideos.tranty.vn.youtuvideos.mics.FontCache;

/**
 * Created by TRUC-SIDA on 4/19/2017.
 */

public class CustomFontTextView extends TextView {

    public static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context, attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context, attrs);
    }

    private void applyCustomFont(Context context, AttributeSet attrs) {
        TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
        String fontName = attributeArray.getString(R.styleable.CustomFontTextView_font);
        int textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);

        Typeface customFont = selectTypeface(context, fontName, textStyle);
        setTypeface(customFont);

        attributeArray.recycle();
    }

    private Typeface selectTypeface(Context context, String fontName, int textStyle) {
        if(fontName.contentEquals(context.getString(R.string.font_roboto))) {
            switch (textStyle) {
                case Typeface.BOLD: // bold
                    return FontCache.getTypeface("Roboto-Bold.ttf", context);
                case Typeface.ITALIC: // italic
                    return FontCache.getTypeface("Roboto-Italic.ttf", context);
                case Typeface.NORMAL: // regular
                default:
                    return FontCache.getTypeface("Roboto-Regular.ttf", context);
            }
        } else {

            return null;
        }
    }
}
