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

package org.catrobat.catroid.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;

public class FormulaEditorCollisionChooseSpriteDialog extends FormulaEditorChooseSpriteDialog {

	public static FormulaEditorCollisionChooseSpriteDialog newInstance() {
		return new FormulaEditorCollisionChooseSpriteDialog();
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		View dialogView = View.inflate(getActivity(), R.layout.dialog_formulaeditor_choose_sprite, null);

		TextView stringName = (TextView) dialogView.findViewById(R.id.string_name);
		stringName.setText(getString(R.string.formula_editor_function_collision));

		setUpSpinner(dialogView);

		final AlertDialog chooseSpriteDialog = new AlertDialog.Builder(getActivity()).setView(dialogView)
				.setTitle(ProjectManager.getInstance().getCurrentSprite().getName())
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).setPositiveButton(R.string.ok, null).create();
		chooseSpriteDialog.setCanceledOnTouchOutside(true);

		chooseSpriteDialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialog) {
				Button ok = chooseSpriteDialog.getButton(AlertDialog.BUTTON_POSITIVE);
				ok.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						chooseSpriteDialog.dismiss();
						success = true;
					}
				});
			}
		});

		return chooseSpriteDialog;
	}
}
