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
 * Copyright (C) 2010 The Android Open Source Project
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
package com.android.quicksearchbox.google;

import com.android.iphonelauncher.R;
import com.android.quicksearchbox.Source;
import com.android.quicksearchbox.SourceResult;

import android.database.DataSetObserver;

public abstract class AbstractGoogleSourceResult implements SourceResult {

    private final Source mSource;
    private final String mUserQuery;
    private int mPos = 0;

    public AbstractGoogleSourceResult(Source source, String userQuery) {
        mSource = source;
        mUserQuery = userQuery;
    }

    public abstract int getCount();

    public abstract String getSuggestionQuery();

    public Source getSource() {
        return mSource;
    }

    public void close() {
    }

    public int getPosition() {
        return mPos;
    }

    public String getUserQuery() {
        return mUserQuery;
    }

    public void moveTo(int pos) {
        mPos = pos;
    }

    public boolean moveToNext() {
        int size = getCount();
        if (mPos >= size) {
            // Already past the end
            return false;
        }
        mPos++;
        return mPos < size;
    }

    public void registerDataSetObserver(DataSetObserver observer) {
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    public String getSuggestionText1() {
        return getSuggestionQuery();
    }

    public Source getSuggestionSource() {
        return mSource;
    }

    public boolean isSuggestionShortcut() {
        return false;
    }

    public String getShortcutId() {
        return null;
    }

    public String getSuggestionFormat() {
        return null;
    }

    public String getSuggestionIcon1() {
        return String.valueOf(R.drawable.magnifying_glass);
    }

    public String getSuggestionIcon2() {
        return null;
    }

    public String getSuggestionIntentAction() {
        return mSource.getDefaultIntentAction();
    }

    public String getSuggestionIntentDataString() {
        return null;
    }

    public String getSuggestionIntentExtraData() {
        return null;
    }

    public String getSuggestionLogType() {
        return null;
    }

    public String getSuggestionText2() {
        return null;
    }

    public String getSuggestionText2Url() {
        return null;
    }

    public boolean isSpinnerWhileRefreshing() {
        return false;
    }

    public boolean isWebSearchSuggestion() {
        return true;
    }

}
