/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2015 The Catrobat Team
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

import android.os.SystemClock;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.assertion.ViewAssertions;
import android.test.suitebuilder.annotation.LargeTest;

import org.catrobat.catroid.ProjectManager;
import org.catrobat.catroid.R;
import org.catrobat.catroid.content.Project;
import org.catrobat.catroid.content.Script;
import org.catrobat.catroid.content.Sprite;
import org.catrobat.catroid.content.StartScript;
import org.catrobat.catroid.content.bricks.HideTextBrick;
import org.catrobat.catroid.content.bricks.SetVariableBrick;
import org.catrobat.catroid.content.bricks.ShowTextBrick;
import org.catrobat.catroid.content.bricks.WaitBrick;
import org.catrobat.catroid.ui.ScriptActivity;
import org.catrobat.catroid.uitest.util.BaseActivityInstrumentationTestCase;
import org.catrobat.catroid.uitest.util.CustomEspressoAction;
import org.catrobat.catroid.uitest.util.UiTestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isClickable;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@LargeTest
public class ShowTextBrickEspressoTest extends BaseActivityInstrumentationTestCase<ScriptActivity> {

	public ShowTextBrickEspressoTest() {
		super(ScriptActivity.class);
	}

	@Override
	public void setUp() throws Exception {
		createProject();
		super.setUp();
		getActivity();
	}

	public void testRun() {
		onView(withId(R.id.brick_show_text_label))
				.check(ViewAssertions.matches(isDisplayed()));

		onView(withId(R.id.button_play)).perform(click());
		pressBack();
		onView(withId(R.id.stage_dialog_button_back)).perform(click());

		onView(withId(R.id.show_text_spinner)).perform(click());
		onView(withId(R.id.dialog_formula_editor_data_name_edit_text)).perform(typeText("ourTestVariable"));
		onView(withText(R.string.ok)).check(matches(isClickable()));
		onView(withText(R.string.ok)).perform(click());

		onView(withText("ourTestVariable")).perform(click());  // only on Android 5.0.1 or higher
		SystemClock.sleep(1000);
		onView(withId(R.id.brick_show_text_prototype_text_view_x)).perform(typeText("100"));
		onView(withId(R.id.brick_show_text_prototype_text_view_y)).perform(typeText("200"));
		SystemClock.sleep(1000);

		onView(withId(R.id.set_variable_spinner)).perform(click());
		onData(withText("ourTestVariable")).perform(click());
//		onView(withText("ourTestVariable")).perform(click());
		onView(withId(R.id.brick_set_variable_prototype_view)).perform(typeText("This is a simple Test"));

		onView(withId(R.id.hide_text_spinner)).perform(click());
		onData(withText("ourTestVariable")).perform(click());

		onView(withId(R.id.button_play)).perform(click());

//		ViewInteraction setVariableBrick = onView(withId(R.id.brick_show_text_label));
//		setVariableBrick.check(ViewAssertions.matches(isDisplayed()));
//
//		setVariableBrick.perform(useCustomEspressoAction());

		SystemClock.sleep(3000);

		// falsche Brick-Instanz
		// TextView existiert nicht
		// Script-Fragment nicht sichtbar
	}

	public static ViewAction useCustomEspressoAction() {
		return new CustomEspressoAction();
	}

	private void createProject() {
		Project project;
		SetVariableBrick setVariableBrick;
		ShowTextBrick showTextBrick;
		HideTextBrick hideTextBrick;
		WaitBrick waitBrick;

		project = new Project(null, UiTestUtils.DEFAULT_TEST_PROJECT_NAME);
		Sprite sprite = new Sprite("cat");
		Script script = new StartScript();

		setVariableBrick = new SetVariableBrick();
		script.addBrick(setVariableBrick);

		showTextBrick = new ShowTextBrick(0, 0);
		script.addBrick(showTextBrick);

		waitBrick = new WaitBrick(3000);
		script.addBrick(waitBrick);

		hideTextBrick = new HideTextBrick();
		script.addBrick(hideTextBrick);

		sprite.addScript(script);
		project.addSprite(sprite);

		ProjectManager.getInstance().setProject(project);
		ProjectManager.getInstance().setCurrentSprite(sprite);
		ProjectManager.getInstance().setCurrentScript(script);
	}
}
