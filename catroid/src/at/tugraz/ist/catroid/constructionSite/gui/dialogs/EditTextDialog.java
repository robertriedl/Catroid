package at.tugraz.ist.catroid.constructionSite.gui.dialogs;

import java.util.HashMap;

import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import at.tugraz.ist.catroid.constructionSite.content.BrickDefine;
import at.tugraz.ist.catroid.R;
import android.view.View.OnClickListener;

public class EditTextDialog extends Dialog implements OnClickListener {
	
	EditText mListEditText;
	EditText mLocalEditText;
	Button mButton;
	HashMap<String,String> mBrickMap;
	boolean isValue1 = false;

	public EditTextDialog(Context context) {
		super(context);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_edit_text);
		mLocalEditText = (EditText) findViewById(R.id.dialogEditText);
		mButton = (Button) findViewById(R.id.dialogEditTextSubmit);
		mButton.setOnClickListener(this);
	}
	
	public void show(HashMap<String,String> brickMap, EditText text) {
		mBrickMap = brickMap;
		mListEditText = text;
		String tag = (String) text.getTag();

		// allow decimal numbers only in wait dialog
		if (tag.equals(getContext().getString(R.string.constructional_brick_wait_edit_text_tag)))
			mLocalEditText.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
		else
			mLocalEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		if (tag.equals(getContext().getString(R.string.constructional_brick_go_to_y_tag)))
			isValue1 = true;
		else
			isValue1 = false;
		if (isValue1)
			mLocalEditText.setText(brickMap.get(BrickDefine.BRICK_VALUE_1));
		else
			mLocalEditText.setText(brickMap.get(BrickDefine.BRICK_VALUE));
		super.show();
		mLocalEditText.requestFocus();
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
		
	}
	
	@Override
	public void onBackPressed() {
		saveContent();
		super.onBackPressed();
	}

	@Override
	public void cancel() {
		saveContent();
		super.cancel();
	}
	
	private void saveContent(){
		if (!(mLocalEditText.getText().toString().length()==0)){
			if (isValue1)
				mBrickMap.put(BrickDefine.BRICK_VALUE_1, mLocalEditText.getText().toString());
			else
				mBrickMap.put(BrickDefine.BRICK_VALUE, mLocalEditText.getText().toString());
			mListEditText.setText(mLocalEditText.getText());
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.dialogEditTextSubmit){
			Log.i("EditTextDialog","in onClickListener");
			cancel();
		}
		
	}

}
