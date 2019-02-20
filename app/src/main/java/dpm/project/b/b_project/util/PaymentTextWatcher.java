package dpm.project.b.b_project.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;

public class PaymentTextWatcher implements TextWatcher {

    EditText editText;
    DecimalFormat decimalFormat;

    public PaymentTextWatcher(EditText et){
        editText = et;
        decimalFormat = new DecimalFormat("###,###");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        editText.removeTextChangedListener(this);
        try {
            int inilen, endlen;
            inilen = editText.getText().length();

            String v = editable.toString().replace(String.valueOf(decimalFormat.getDecimalFormatSymbols().getGroupingSeparator()), "");
            Number n = decimalFormat.parse(v);
            int cp = editText.getSelectionStart();
            editText.setText(decimalFormat.format(n));
            endlen = editText.getText().length();
            int sel = (cp + (endlen - inilen));
            if (sel > 0 && sel <= editText.getText().length()) {
                editText.setSelection(sel);
            } else {
                // place cursor at the end?
                editText.setSelection(editText.getText().length() - 1);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        editText.addTextChangedListener(this);
    }
}
