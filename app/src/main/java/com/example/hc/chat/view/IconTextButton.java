package com.example.hc.chat.view;

/**
 * Created by hc on 2016/9/7.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.hc.chat.R;

/**
 * Created by hc on 2016/8/17.
 *
 * 一个 上面是图标，下面是文字的自定义 view
 *
 * setIcon() 设置图片
 * setText() 设置文本 //这个方法是 Button 自带的
 */
public class IconTextButton extends Button {

    private int resourceId = 0;
    private Bitmap bitmap;
    private int textWidth;
    private int textHeight;

    public IconTextButton(Context context) {
        super(context,null);
    }

    public IconTextButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        //this.setClickable(true);
        resourceId = R.drawable.tmp;
        bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
    }

    public void setIcon(int resourceId) {
        Log.d("viewtest","-------setIcon()");
        this.bitmap = BitmapFactory.decodeResource(getResources(), resourceId);
        requestLayout();
        invalidate();
    }


    @Override
    protected  void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        Log.d("viewtest","-----onMeasure()");
        Log.d("viewtest","onMeasure() , bitmapHeiht ="+bitmap.getHeight()+", bitmapWidth ="+bitmap.getWidth());


        // int mHeigth = Math.max(bitmap.getHeight(), (int)this.getTextSize());
        String textString =  this.getText().toString();
        Paint paint = new Paint();
        paint.setTextSize(this.getTextSize());

        Paint.FontMetrics fm = paint.getFontMetrics();
        textHeight = (int)(fm.bottom - fm.top);
        //int mHeigth = (int)Math.max((fm.bottom - fm.top), bitmap.getHeight());
        int mHeigth = textHeight + bitmap.getHeight();

        /**
         * 若计算的高度大于父控件的高度，图片缩小 0.8
         */
        if( ((View)getParent()).getHeight() > 0) {
            if (mHeigth > ((View) getParent()).getHeight()) {
                Matrix matrix = new Matrix();
                matrix.setScale(0.6f, 0.6f);
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            }
        }

        textWidth = (int) paint.measureText(textString);
        //int mWidth = textWidth + bitmap.getWidth();
        int mWidth = (int)Math.max(textWidth, bitmap.getWidth())+getPaddingLeft()+getPaddingRight();
        Log.d("icontexttest","textwidth :"+textWidth+",bitmapwidth :"+bitmap.getWidth()+",mwidth :"+mWidth);

        Log.d("viewtest","onMeasure(), mHeight ="+mHeigth+",mWidth ="+mWidth+",parentHeight ="+ ((View)getParent()).getMeasuredHeight());
        Log.d("viewtest","onMeasure() , bitmapHeiht ="+bitmap.getHeight()+", bitmapWidth ="+bitmap.getWidth());

        if(heightSpecMode == MeasureSpec.AT_MOST && widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, mHeigth);
        }else if(heightSpecMode == MeasureSpec.AT_MOST ){
            setMeasuredDimension(widthSpecSize, mHeigth);
        }else if(widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(mWidth, heightSpecSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        Log.d("viewtest","---------onDraw() , bitmapHeiht ="+bitmap.getHeight()+", bitmapWidth ="+bitmap.getWidth());
        Log.d("viewtest","---------onDraw() , getMeasuredHeight() ="+getMeasuredHeight()+", getMeasuredWidth() ="+getMeasuredWidth());
        // 图片水平方向居中，竖直方向靠上
        int y = 0;
        int x = this.getMeasuredWidth()/2 - bitmap.getWidth()/2;
        canvas.drawBitmap(bitmap, x, y, null);
        // 坐标需要转换，因为默认情况下Button中的文字居中显示
        // 这里需要让文字靠下
        canvas.translate(  0, (this.getMeasuredHeight() - textHeight)/2  );
        super.onDraw(canvas);
    }

}