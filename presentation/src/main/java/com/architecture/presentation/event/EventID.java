package com.architecture.presentation.event;


public interface EventID {
    int BASE = 1;

    int APP_BECOME_FOREGROUND = 2;
    int APP_INTO_BACKGROUND = 3;
    int USER_CHANGED = 4;

    int MAX = 5;
}
