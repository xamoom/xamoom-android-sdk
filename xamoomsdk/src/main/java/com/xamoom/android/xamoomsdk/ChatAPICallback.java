/*
 * Copyright (c) 2017 xamoom GmbH <apps@xamoom.com>
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at the root of this project.
 */

package com.xamoom.android.xamoomsdk;

import java.util.ArrayList;

/**
 * Generic Callback for {@link EnduserApi} chat api calls.
 */
public interface ChatAPICallback {
    void finished(String text, String context, ArrayList<ChatbotAction> actions, double confidence, boolean success);
    void error(String status_code, String message);
}

