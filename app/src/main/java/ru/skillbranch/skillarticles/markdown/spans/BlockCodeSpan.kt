package ru.skillbranch.skillarticles.markdown.spans

import android.graphics.*
import android.text.style.ReplacementSpan
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.annotation.VisibleForTesting
import ru.skillbranch.skillarticles.markdown.Element


class BlockCodeSpan(
    @ColorInt
    private val textColor: Int,
    @ColorInt
    private val bgColor: Int,
    @Px
    private val cornerRadius: Float,
    @Px
    private val padding: Float,
    private val type: Element.BlockCode.Type
) : ReplacementSpan() {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var rect = RectF()
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var path = Path()

    override fun draw(
        canvas: Canvas,
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        top: Int,
        y: Int,
        bottom: Int,
        paint: Paint
    ) {
        paint.forRectangle {
            when(type){
                Element.BlockCode.Type.SINGLE -> {
                    canvas.drawRoundRect(
                        RectF(
                            0f,
                            top + padding,
                            canvas.width.toFloat(),
                            bottom - padding
                        ),
                        cornerRadius,
                        cornerRadius,
                        paint
                    )
                }
                Element.BlockCode.Type.MIDDLE ->{
                    canvas.drawRect(
                        RectF(
                            0f,
                            top.toFloat(),
                            canvas.width.toFloat(),
                            bottom.toFloat()
                        ),
                        paint
                    )
                }
            }
        }

        paint.forText {
            canvas.drawText(text, 0, text.length, x + padding, y.toFloat(), paint)
            when(type){
                Element.BlockCode.Type.START -> {
                    path.reset()
                    path.addRoundRect(
                        RectF(
                            0f,
                            top + padding,
                            canvas.width.toFloat(),
                            bottom.toFloat()
                        ),
                        floatArrayOf(
                            cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius,
                            0f, 0f,
                            0f, 0f
                        ),
                        Path.Direction.CW
                    )
                    canvas.drawPath(path, paint)
                }
                Element.BlockCode.Type.END ->{
                    path.reset()
                    path.addRoundRect(
                        RectF(
                            0f,
                            top.toFloat(),
                            canvas.width.toFloat(),
                            bottom - padding
                        ),
                        floatArrayOf(
                            0f, 0f,
                            0f, 0f,
                            cornerRadius, cornerRadius,
                            cornerRadius, cornerRadius
                        ),
                        Path.Direction.CW
                    )
                    canvas.drawPath(path, paint)
                }
            }
        }
    }

    private inline fun Paint.forRectangle(block: () -> Unit) {
        color = bgColor
        block()
    }

    private inline fun Paint.forText(block: () -> Unit) {
        color = textColor
        block()
        color = Color.GRAY
    }

    override fun getSize(
        paint: Paint,
        text: CharSequence,
        start: Int,
        end: Int,
        fm: Paint.FontMetricsInt?
    ): Int {
        when(type){
            Element.BlockCode.Type.SINGLE -> {
                fm!!.ascent = (fm.ascent * 0.85f - 2 * padding).toInt()
                fm.descent = (fm.descent * 0.85f + 2 * padding).toInt()
            }
            Element.BlockCode.Type.START -> {
                fm!!.ascent = (paint.ascent() - 2 * padding).toInt()
                fm.descent = paint.descent().toInt()
            }
            Element.BlockCode.Type.MIDDLE ->{
                fm!!.ascent = (fm.ascent * 0.85f).toInt()
                fm.descent = (fm.descent * 0.85f).toInt()
            }
            Element.BlockCode.Type.END ->{
                fm!!.ascent = (fm.ascent * 0.85f).toInt()
                fm.descent = (fm.descent * 0.85f + 2 * padding).toInt()
            }
        }
        return 0
    }
}
