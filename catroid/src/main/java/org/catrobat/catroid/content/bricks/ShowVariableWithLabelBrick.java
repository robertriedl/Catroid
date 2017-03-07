/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2017 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.catrobat.catroid.content.bricks;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;

import org.catrobat.catroid.CatroidApplication;
import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.common.BrickValues;
import org.catrobat.catroid.common.Constants;
import org.catrobat.catroid.content.Scene;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.formulaeditor.Formula;
import org.catrobat.catroid.formulaeditor.FormulaElement;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.ui.adapter.DataAdapter;
import org.catrobat.catroid.ui.adapter.UserVariableAdapterWrapper;
import org.catrobat.catroid.ui.dialogs.NewDataDialog;
import org.catrobat.catroid.ui.fragment.ColorSeekbar;
import org.catrobat.catroid.ui.fragment.FormulaEditorFragment;
import org.catrobat.catroid.utils.Utils;

import java.util.List;

public class ShowVariableWithLabelBrick extends ShowTextBrick {

	private static final long serialVersionUID = 1L;

	private transient View prototypeView;

	public static final String TAG = ShowVariableWithLabelBrick.class.getSimpleName();

	public ShowVariableWithLabelBrick(int xPosition, int yPosition, int textSize, float red, float green, float blue) {
		initializeBrickFields(new Formula(xPosition), new Formula(yPosition), new Formula(textSize),
				new Formula(red), new Formula(green), new Formula(blue));
	}

	public ShowVariableWithLabelBrick(Formula xPosition, Formula yPosition, Formula textSize, Formula red, Formula green,
			Formula blue) {
		initializeBrickFields(xPosition, yPosition, textSize, red, green, blue);
	}

	private void initializeBrickFields(Formula xPosition, Formula yPosition, Formula textSize, Formula red, Formula
			green, Formula blue) {
		addAllowedBrickField(BrickField.X_POSITION);
		addAllowedBrickField(BrickField.Y_POSITION);
		addAllowedBrickField(BrickField.SHOWVARIABLE_SIZE);
		addAllowedBrickField(BrickField.SHOWVARIABLE_COLOR_RED);
		addAllowedBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN);
		addAllowedBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE);
		setFormulaWithBrickField(BrickField.X_POSITION, xPosition);
		setFormulaWithBrickField(BrickField.Y_POSITION, yPosition);
		setFormulaWithBrickField(BrickField.SHOWVARIABLE_SIZE, textSize);
		setFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_RED, red);
		setFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN, green);
		setFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE, blue);
	}

	@Override
	public int getRequiredResources() {
		return getFormulaWithBrickField(BrickField.Y_POSITION).getRequiredResources() |
				getFormulaWithBrickField(BrickField.X_POSITION).getRequiredResources() |
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_SIZE).getRequiredResources() |
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_RED).getRequiredResources() |
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN).getRequiredResources() |
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE).getRequiredResources();
	}

	@Override
	public View getView(final Context context, int brickId, BaseAdapter baseAdapter) {
		if (animationState) {
			return view;
		}

		view = View.inflate(context, R.layout.brick_show_variable_with_label, null);
		view = BrickViewProvider.setAlphaOnView(view, alphaValue);

		setCheckboxView(R.id.brick_show_variable_with_label_checkbox);

		TextView editTextXPosition = (TextView) view.findViewById(R.id.brick_show_variable_with_label_edit_text_x);
		getFormulaWithBrickField(BrickField.X_POSITION).setTextFieldId(R.id.brick_show_variable_with_label_edit_text_x);
		getFormulaWithBrickField(BrickField.X_POSITION).refreshTextField(view);
		editTextXPosition.setOnClickListener(this);

		TextView editTextYPosition = (TextView) view.findViewById(R.id.brick_show_variable_with_label_edit_text_y);
		getFormulaWithBrickField(BrickField.Y_POSITION).setTextFieldId(R.id.brick_show_variable_with_label_edit_text_y);
		getFormulaWithBrickField(BrickField.Y_POSITION).refreshTextField(view);
		editTextYPosition.setOnClickListener(this);

		Spinner showVariableSpinner = (Spinner) view.findViewById(R.id.show_variable_spinner);

		UserBrick currentBrick = ProjectManager.getInstance().getCurrentUserBrick();

		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentScene().getDataContainer()
				.createDataAdapter(context, currentBrick, ProjectManager.getInstance().getCurrentSprite());
		UserVariableAdapterWrapper userVariableAdapterWrapper = new UserVariableAdapterWrapper(context,
				dataAdapter);
		userVariableAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);

		showVariableSpinner.setAdapter(userVariableAdapterWrapper);
		setSpinnerSelection(showVariableSpinner, null);

		showVariableSpinner.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN
						&& (((Spinner) view).getSelectedItemPosition() == 0
						&& ((Spinner) view).getAdapter().getCount() == 1)) {
					NewDataDialog dialog = new NewDataDialog((Spinner) view, NewDataDialog.DialogType.USER_VARIABLE);
					dialog.addVariableDialogListener(ShowVariableWithLabelBrick.this);
					dialog.show(((Activity) view.getContext()).getFragmentManager(), NewDataDialog.DIALOG_FRAGMENT_TAG);
					return true;
				}

				return false;
			}
		});

		showVariableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if (position == 0 && ((UserVariableAdapterWrapper) parent.getAdapter()).isTouchInDropDownView()) {
					NewDataDialog dialog = new NewDataDialog((Spinner) parent, NewDataDialog.DialogType.USER_VARIABLE);
					dialog.addVariableDialogListener(ShowVariableWithLabelBrick.this);
					int spinnerPos = ((UserVariableAdapterWrapper) parent.getAdapter())
							.getPositionOfItem(userVariable);
					dialog.setUserVariableIfCancel(spinnerPos);
					dialog.show(((Activity) view.getContext()).getFragmentManager(),
							NewDataDialog.DIALOG_FRAGMENT_TAG);
				}
				((UserVariableAdapterWrapper) parent.getAdapter()).resetIsTouchInDropDownView();
				userVariable = (UserVariable) parent.getItemAtPosition(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				userVariable = (UserVariable) arg0.getItemAtPosition(1);
			}
		});

		TextView editTextSize = (TextView) view.findViewById(R.id.brick_show_variable_with_label_size_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_SIZE).setTextFieldId(R.id.brick_show_variable_with_label_size_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_SIZE).refreshTextField(view);
		editTextSize.setOnClickListener(this);

		TextView textViewColorRed = (TextView) view.findViewById(R.id.brick_show_variable_with_label_color_red_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_RED).setTextFieldId(R.id.brick_show_variable_with_label_color_red_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_RED).refreshTextField(view);
		textViewColorRed.setOnClickListener(this);

		TextView textViewColorGreen = (TextView) view.findViewById(R.id.brick_show_variable_with_label_color_green_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN).setTextFieldId(R.id.brick_show_variable_with_label_color_green_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN).refreshTextField(view);
		textViewColorGreen.setOnClickListener(this);

		TextView textViewColorBlue = (TextView) view.findViewById(R.id.brick_show_variable_with_label_color_blue_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE).setTextFieldId(R.id.brick_show_variable_with_label_color_blue_edit_text);
		getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE).refreshTextField(view);
		textViewColorBlue.setOnClickListener(this);

		return view;
	}

	@Override
	public View getPrototypeView(Context context) {
		prototypeView = View.inflate(context, R.layout.brick_show_variable_with_label, null);

		Spinner variableSpinner = (Spinner) prototypeView.findViewById(R.id.show_variable_spinner);
		UserBrick currentBrick = ProjectManager.getInstance().getCurrentUserBrick();

		DataAdapter dataAdapter = ProjectManager.getInstance().getCurrentScene().getDataContainer()
				.createDataAdapter(context, currentBrick, ProjectManager.getInstance().getCurrentSprite());

		UserVariableAdapterWrapper userVariableAdapterWrapper = new UserVariableAdapterWrapper(context,
				dataAdapter);

		userVariableAdapterWrapper.setItemLayout(android.R.layout.simple_spinner_item, android.R.id.text1);
		variableSpinner.setAdapter(userVariableAdapterWrapper);
		setSpinnerSelection(variableSpinner, null);

		TextView textViewPositionX = (TextView) prototypeView.findViewById(R.id.brick_show_variable_with_label_edit_text_x);
		textViewPositionX.setText(Utils.getNumberStringForBricks(BrickValues.X_POSITION));
		TextView textViewPositionY = (TextView) prototypeView.findViewById(R.id.brick_show_variable_with_label_edit_text_y);
		textViewPositionY.setText(Utils.getNumberStringForBricks(BrickValues.Y_POSITION));

		TextView textViewSizeInPercent = (TextView) prototypeView.findViewById(R.id.brick_show_variable_with_label_size_edit_text);
		textViewSizeInPercent.setText(Utils.getNumberStringForBricks(BrickValues.SHOW_VARIABLE_TEXT_SIZE));

		TextView textViewColorRed = (TextView) prototypeView.findViewById(R.id
				.brick_show_variable_with_label_color_red_edit_text);
		textViewColorRed.setText(String.valueOf(BrickValues.SHOW_VARIABLE_TEXT_COLOR_RED));
		TextView textViewColorGreen = (TextView) prototypeView.findViewById(R.id
				.brick_show_variable_with_label_color_green_edit_text);
		textViewColorGreen.setText(String.valueOf(BrickValues.SHOW_VARIABLE_TEXT_COLOR_GREEN));
		TextView textViewColorBlue = (TextView) prototypeView.findViewById(R.id
				.brick_show_variable_with_label_color_blue_edit_text);
		textViewColorBlue.setText(String.valueOf(BrickValues.SHOW_VARIABLE_TEXT_COLOR_BLUE));

		return prototypeView;
	}

	@Override
	public Brick clone() {
		return new ShowVariableWithLabelBrick(getFormulaWithBrickField(BrickField.X_POSITION).clone(),
				getFormulaWithBrickField(BrickField.Y_POSITION).clone(),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_SIZE).clone(),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_RED).clone(),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN).clone(),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE).clone());
	}

	@Override
	public List<SequenceAction> addActionToSequence(Sprite sprite, SequenceAction sequence) {
		if (this.userVariable == null || this.userVariable.getName() == null) {
			noVariableSelected(CatroidApplication.getAppContext());
		} else {
			showLabel = true;
		}
		sequence.addAction(sprite.getActionFactory().createShowVariableAction(sprite,
				getFormulaWithBrickField(BrickField.X_POSITION),
				getFormulaWithBrickField(BrickField.Y_POSITION),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_SIZE),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_RED),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_GREEN),
				getFormulaWithBrickField(BrickField.SHOWVARIABLE_COLOR_BLUE), userVariable, showLabel));
		return null;
	}

	private void combineVariableAndLabel () {
		String nameAndLabel = this.userVariable.getName().toString() + " . " + userVariable.getValue().toString();
		this.userVariable.setValue(nameAndLabel);


	}
}
