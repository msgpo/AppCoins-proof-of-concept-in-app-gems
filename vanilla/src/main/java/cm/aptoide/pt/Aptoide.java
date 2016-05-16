/*
 * Copyright (c) 2016.
 * Modified by Neurophobic Animal on 10/05/2016.
 */

package cm.aptoide.pt;

import cm.aptoide.pt.preferences.AptoideConfiguration;
import cm.aptoide.pt.v8engine.V8Engine;

/**
 * Created by neuro on 10-05-2016.
 */
public class Aptoide extends V8Engine {

	@Override
	protected AptoideConfiguration createConfiguration() {
		return new VanillaConfiguration();
	}
}