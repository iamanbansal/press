package press.sync

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color.BLACK
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.core.view.updatePadding
import com.google.android.material.textfield.TextInputLayout
import me.saket.press.R
import me.saket.press.shared.theme.TextStyles.smallBody
import me.saket.press.shared.theme.applyStyle
import me.saket.press.shared.theme.blendWith
import press.extensions.textColor
import press.theme.themeAware
import press.widgets.dp

class SearchView(context: Context) : TextInputLayout(context) {
  init {
    addView(EditText(context).apply {
      id = R.id.search_view_field
      background = null
      imeOptions = imeOptions or EditorInfo.IME_FLAG_NO_FULLSCREEN
      applyStyle(smallBody)
      themeAware {
        textColor = it.textColorPrimary
      }
      updatePadding(top = dp(24), bottom = dp(8), left = dp(16), right = dp(16))
    })

    endIconMode = END_ICON_CLEAR_TEXT
    importantForAutofill = IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS

    boxBackgroundMode = BOX_BACKGROUND_FILLED
    themeAware {
      hintTextColor = ColorStateList.valueOf(it.accentColor)
      boxBackgroundColor = it.window.backgroundColor.blendWith(BLACK, ratio = 0.1f)
      boxStrokeColor = it.accentColor

      // Note to self: this is not the same as hintTextColor
      setEndIconTintList(ColorStateList.valueOf(editText!!.currentHintTextColor))
    }
  }
}
