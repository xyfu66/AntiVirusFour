// IPackageStatsObserver.aidl
package com.jinyun.antivirusfour;

// Declare any non-default types here with import statements

interface IPackageStatsObserver {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void onGetStatsCompleted(in PackageStats pStats, boolean succeeded);
}
