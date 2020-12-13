package org.http4k.connect.google.analytics

import dev.forkhandles.result4k.Result
import org.http4k.connect.RemoteFailure
import org.http4k.connect.google.analytics.action.GoogleAnalyticsAction

interface GoogleAnalytics {
    /**
     * Available actions:
     *  PageView
     */
    operator fun <R> invoke(request: GoogleAnalyticsAction<R>): Result<R, RemoteFailure>

    companion object
}
