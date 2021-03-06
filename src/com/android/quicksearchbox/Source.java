/* Copyright Statement:
 *
 * This software/firmware and related documentation ("MediaTek Software") are
 * protected under relevant copyright laws. The information contained herein
 * is confidential and proprietary to MediaTek Inc. and/or its licensors.
 * Without the prior written permission of MediaTek inc. and/or its licensors,
 * any reproduction, modification, use or disclosure of MediaTek Software,
 * and information contained herein, in whole or in part, shall be strictly prohibited.
 *
 * MediaTek Inc. (C) 2010. All rights reserved.
 *
 * BY OPENING THIS FILE, RECEIVER HEREBY UNEQUIVOCALLY ACKNOWLEDGES AND AGREES
 * THAT THE SOFTWARE/FIRMWARE AND ITS DOCUMENTATIONS ("MEDIATEK SOFTWARE")
 * RECEIVED FROM MEDIATEK AND/OR ITS REPRESENTATIVES ARE PROVIDED TO RECEIVER ON
 * AN "AS-IS" BASIS ONLY. MEDIATEK EXPRESSLY DISCLAIMS ANY AND ALL WARRANTIES,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR NONINFRINGEMENT.
 * NEITHER DOES MEDIATEK PROVIDE ANY WARRANTY WHATSOEVER WITH RESPECT TO THE
 * SOFTWARE OF ANY THIRD PARTY WHICH MAY BE USED BY, INCORPORATED IN, OR
 * SUPPLIED WITH THE MEDIATEK SOFTWARE, AND RECEIVER AGREES TO LOOK ONLY TO SUCH
 * THIRD PARTY FOR ANY WARRANTY CLAIM RELATING THERETO. RECEIVER EXPRESSLY ACKNOWLEDGES
 * THAT IT IS RECEIVER'S SOLE RESPONSIBILITY TO OBTAIN FROM ANY THIRD PARTY ALL PROPER LICENSES
 * CONTAINED IN MEDIATEK SOFTWARE. MEDIATEK SHALL ALSO NOT BE RESPONSIBLE FOR ANY MEDIATEK
 * SOFTWARE RELEASES MADE TO RECEIVER'S SPECIFICATION OR TO CONFORM TO A PARTICULAR
 * STANDARD OR OPEN FORUM. RECEIVER'S SOLE AND EXCLUSIVE REMEDY AND MEDIATEK'S ENTIRE AND
 * CUMULATIVE LIABILITY WITH RESPECT TO THE MEDIATEK SOFTWARE RELEASED HEREUNDER WILL BE,
 * AT MEDIATEK'S OPTION, TO REVISE OR REPLACE THE MEDIATEK SOFTWARE AT ISSUE,
 * OR REFUND ANY SOFTWARE LICENSE FEES OR SERVICE CHARGE PAID BY RECEIVER TO
 * MEDIATEK FOR SUCH MEDIATEK SOFTWARE AT ISSUE.
 */

/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.quicksearchbox;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

/**
 * Interface for suggestion sources.
 *
 */
public interface Source extends SuggestionCursorProvider<SourceResult> {

    /**
     * Gets the name activity that intents from this source are sent to.
     */
    ComponentName getIntentComponent();

    /**
     * Gets the version code of the source. This is expected to change when the app that
     * this source is for is upgraded.
     */
    int getVersionCode();

    /**
     * Indicates if shortcuts from the given version of this source are compatible with the
     * currently installed version. The version code given will only differ from the currently
     * installed version after the source has been upgraded.
     *
     * @param version version of the source (as returned by {@link #getVersionCode} which originally
     *      created the shortcut.
     */
    boolean isVersionCodeCompatible(int version);

    /**
     * Gets the localized, human-readable label for this source.
     */
    CharSequence getLabel();

    /**
     * Gets the icon for this suggestion source.
     */
    Drawable getSourceIcon();

    /**
     * Gets the icon URI for this suggestion source.
     */
    Uri getSourceIconUri();

    /**
     * Gets an icon from this suggestion source.
     *
     * @param drawableId Resource ID or URI.
     */
    Drawable getIcon(String drawableId);

    /**
     * Gets the URI for an icon form this suggestion source.
     *
     * @param drawableId Resource ID or URI.
     */
    Uri getIconUri(String drawableId);

    /**
     * Gets the search hint text for this suggestion source.
     */
    CharSequence getHint();

    /**
     * Gets the description to use for this source in system search settings.
     */
    CharSequence getSettingsDescription();

    /**
     *
     *  Note: this does not guarantee that this source will be queried for queries of
     *  this length or longer, only that it will not be queried for anything shorter.
     *
     * @return The minimum number of characters needed to trigger this source.
     */
    int getQueryThreshold();

    /**
     * Indicates whether a source should be invoked for supersets of queries it has returned zero
     * results for in the past.  For example, if a source returned zero results for "bo", it would
     * be ignored for "bob".
     *
     * If set to <code>false</code>, this source will only be ignored for a single session; the next
     * time the search dialog is brought up, all sources will be queried.
     *
     * @return <code>true</code> if this source should be queried after returning no results.
     */
    boolean queryAfterZeroResults();

    boolean voiceSearchEnabled();

    boolean isWebSuggestionSource();

    boolean isLocationAware();

    Intent createSearchIntent(String query, Bundle appData);

    Intent createVoiceSearchIntent(Bundle appData);

    /**
     * Checks if the current process can read the suggestions from this source.
     */
    boolean canRead();

    /**
     * Gets suggestions from this source.
     *
     * @param query The user query.
     * @param queryLimit An advisory maximum number of results that the source should return.
     * @param onlySource Indicates if this is the only source being queried.
     * @return The suggestion results.
     */
    SourceResult getSuggestions(String query, int queryLimit, boolean onlySource);

    /**
     * Updates a shorcut.
     *
     * @param shortcutId The id of the shortcut to update.
     * @param extraData associated with this shortcut.
     * @return A SuggestionCursor positioned at the updated shortcut.  If the
     *         cursor is empty or <code>null</code>, the shortcut will be removed.
     */
    SuggestionCursor refreshShortcut(String shortcutId, String extraData);

    /**
     * Gets the default intent action for suggestions from this source.
     *
     * @return The default intent action, or {@code null}.
     */
    String getDefaultIntentAction();

    /**
     * Gets the default intent data for suggestions from this source.
     *
     * @return The default intent data, or {@code null}.
     */
    String getDefaultIntentData();

}
