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

package org.catrobat.catroid.uitest.util;

import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;

public class CustomEspressoAction implements ViewAction {
	private int testNumber = 11;

	public CustomEspressoAction() {

		testNumber = 22;
	}

	public int getCount() {
		return testNumber;
	}

	@Override
	public Matcher<View> getConstraints() {
		return ViewMatchers.isDisplayed();
	}

	@Override
	public String getDescription() {
		return "Custom Action!";
	}

	@Override
	public void perform(UiController uiController, View view) {
		Log.d("custom", "perform called " + testNumber);
	}
}
