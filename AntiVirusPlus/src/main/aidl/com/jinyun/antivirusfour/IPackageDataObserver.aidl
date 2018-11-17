// IPackageDataObserver.aidl
package com.jinyun.antivirusfour;

// Declare any non-default types here with import statements

interface IPackageDataObserver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onRemoveCompleted(in String packageName, boolean succeeded);
}
