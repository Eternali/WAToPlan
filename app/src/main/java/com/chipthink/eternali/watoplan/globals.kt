package com.chipthink.eternali.watoplan

/**
 * Package-wide constants, methods, etc.
 * --------IMPORTANT---------------
 * When I refer to 'activity' in comments, I most likely will be refering to
 * the datatypes the user creates
 * (i.e. all events, meetings, games, projects, tests are implementations of base Activity)
 * --------/IMPORTANT--------------
 */

/**
 * General structure of app architecture:
 * data - contains interface to database
 * models - contains the possible states of the application
 * viewmodels - the observable that view subscribe to
 * views - interfaces that specify required view methods as well as intents.
 *      activities/fragments/etc. - actual view classes to render to display
 */
