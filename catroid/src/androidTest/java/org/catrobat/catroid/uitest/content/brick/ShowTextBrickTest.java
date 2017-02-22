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

package org.catrobat.catroid.uitest.content.brick;

import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.SingleSprite;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.Brick;
import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.UserVariableBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.formulaeditor.UserVariable;
import org.catrobat.catroid.test.utils.Reflection;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.ui.adapter.BrickAdapter;
import org.catrobat.catroid.uitest.util.BaseActivityInstrumentationTestCase;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import java.util.ArrayList;

public class ShowTextBrickTest extends BaseActivityInstrumentationTestCase<ScriptActivity> {

	private static final int X_POSITION = 0;
	private static final int Y_POSITION = 0;
	private static final int TEXT_SIZE = 100;
	private static final float RED = 255f;
	private static final float GREEN = 255f;
	private static final float BLUE = 255f;
	private static final double COLOR_SEEK_BAR_VALUE = 127d;
	private static final double COLOR_SEEK_BAR_VALUE_THRESHOLD = 1d;

	private Project project;
	private SetVariableBrick setVariableBrick;
	private ShowTextBrick showTextBrick;
	private HideTextBrick hideTextBrick;
	private WaitBrick waitBrick;

	public ShowTextBrickTest() {
		super(ScriptActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		// normally super.setUp should be called first
		// but kept the test failing due to view is null
		// when starting in ScriptActivity
		createProject();
		super.setUp();
	}

	public void testShowHideBrick() {
		ListView dragDropListView = UiTestUtils.getScriptListView(solo);
		BrickAdapter adapter = (BrickAdapter) dragDropListView.getAdapter();

		int childrenCount = adapter.getChildCountFromLastGroup();
		int groupCount = adapter.getScriptCount();

		assertEquals("Incorrect number of bricks.", 4, dragDropListView.getChildCount());
		assertEquals("Incorrect number of bricks.", 3, childrenCount);

		ArrayList<Brick> projectBrickList = project.getDefaultScene().getSpriteList().get(0).getScript(0).getBrickList();
		assertEquals("Incorrect number of bricks.", 3, projectBrickList.size());

		assertEquals("Wrong Brick instance.", projectBrickList.get(0), adapter.getChild(groupCount - 1, 0));
		assertNotNull("TextView does not exist.", solo.getText(solo.getString(R.string.brick_set_variable)));

		assertTrue("ScriptFragment not visible", solo.waitForText(solo.getString(R.string.brick_set_variable)));

		UiTestUtils.testBrickWithFormulaEditor(solo, ProjectManager.getInstance().getCurrentSprite(),
				R.id.brick_show_variable_edit_text_x, 111, Brick.BrickField.X_POSITION, showTextBrick);
		UiTestUtils.testBrickWithFormulaEditor(solo, ProjectManager.getInstance().getCurrentSprite(),
				R.id.brick_show_variable_edit_text_y, 222, Brick.BrickField.Y_POSITION, showTextBrick);

		UiTestUtils.testBrickWithFormulaEditor(solo, ProjectManager.getInstance().getCurrentSprite(),
				R.id.brick_show_variable_size_edit_text, 200, Brick.BrickField.SHOWVARIABLE_SIZE, showTextBrick);

		solo.clickOnText(getInstrumentation().getTargetContext().getString(
				R.string.brick_variable_spinner_create_new_variable), 1, true);
		EditText varName = (EditText) solo.getView(R.id.dialog_formula_editor_data_name_edit_text);
		solo.enterText(varName, "newVar");
		solo.clickOnButton(solo.getString(R.string.ok));
//		solo.clickOnText("newVar"); // ONLY on Android 5.0.1

		UiTestUtils.testBrickWithFormulaEditor(solo, ProjectManager.getInstance().getCurrentSprite(),
				R.id.brick_set_variable_edit_text, 12345, Brick.BrickField.VARIABLE, setVariableBrick);

		solo.clickOnText("newVar", 3, true);
		solo.clickOnText("newVar", 1, true);

		UserVariable userVariable = (UserVariable) Reflection.getPrivateField(UserVariableBrick.class, setVariableBrick,
				"userVariable");
		assertNotNull("UserVariable is null", userVariable);
		assertTrue("UserVariable Name not as expected", userVariable.getName().equals("newVar"));
	}

	public void testRedColorSeekBar() {
		testColorSeekBar(R.id.brick_show_variable_color_red_edit_text, R.id.color_rgb_seekbar_red, R.id.rgb_red_value);
	}

	public void testGreenColorSeekBar() {
		testColorSeekBar(R.id.brick_show_variable_color_green_edit_text, R.id.color_rgb_seekbar_green, R.id.rgb_green_value);
	}

	public void testBlueColorSeekBar() {
		testColorSeekBar(R.id.brick_show_variable_color_blue_edit_text, R.id.color_rgb_seekbar_blue, R.id.rgb_blue_value);
	}

	private void testColorSeekBar(int editText, int colorSeekBar, int colorSeekBarEditText) {
		solo.clickOnView(solo.getView(editText));
		solo.clickOnView(solo.getView(colorSeekBar));
		solo.clickOnView(solo.getView(colorSeekBarEditText));

		assertEquals("Text not updated within FormulaEditor", COLOR_SEEK_BAR_VALUE,
				Double.parseDouble(((EditText) solo.getView(R.id.formula_editor_edit_field)).getText().toString()
						.replace(',', '.')), COLOR_SEEK_BAR_VALUE_THRESHOLD);
	}

	public void testRedFormulaEditorInput() {
		testFormulaEditorInput(R.id.brick_show_variable_color_red_edit_text, R.id.rgb_red_value);
	}

	public void testGreenFormulaEditorInput() {
		testFormulaEditorInput(R.id.brick_show_variable_color_green_edit_text, R.id.rgb_green_value);
	}

	public void testBlueFormulaEditorInput() {
		testFormulaEditorInput(R.id.brick_show_variable_color_blue_edit_text, R.id.rgb_blue_value);
	}

	private void testFormulaEditorInput(int editText, int colorSeekBarEditText) {
		solo.clickOnView(solo.getView(editText));
		solo.clickOnView(solo.getView(colorSeekBarEditText));

		UiTestUtils.insertDoubleIntoEditText(solo, 100.0);
		solo.clickOnView(solo.getView(R.id.formula_editor_keyboard_ok));

		solo.waitForView(editText);
		TextView textView = ((TextView) solo.getView(editText));
		assertEquals("Text not updated", 100.0, Double.parseDouble(textView.getText().toString().replace(',', '.')));
	}

	private void createProject() {
		project = new Project(null, UiTestUtils.DEFAULT_TEST_PROJECT_NAME);
		Sprite sprite = new SingleSprite("cat");
		Script script = new StartScript();

		setVariableBrick = new SetVariableBrick();
		script.addBrick(setVariableBrick);

		showTextBrick = new ShowTextBrick(X_POSITION, Y_POSITION, TEXT_SIZE, RED, GREEN, BLUE);
		script.addBrick(showTextBrick);

		hideTextBrick = new HideTextBrick();
		script.addBrick(hideTextBrick);

		sprite.addScript(script);
		project.getDefaultScene().addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);
	}
}
