package com.dimi.themoviedb.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Parcelable
import android.util.Log
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.dimi.themoviedb.R
import com.dimi.themoviedb.models.PassingModel
import com.dimi.themoviedb.ui.main.tv_show.ViewEpisodeFragment
import com.dimi.themoviedb.ui.main.tv_show.ViewSeasonFragment
import com.dimi.themoviedb.utils.BottomNavController.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.parcel.Parcelize
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Class credit: Allan Veloso
 * I took the concept from Allan Veloso and made alterations to fit our needs.
 * https://stackoverflow.com/questions/50577356/android-jetpack-navigation-bottomnavigationview-with-youtube-or-instagram-like#_=_
 * @property navigationBackStack: Backstack for the bottom navigation
 */

const val BOTTOM_NAV_BACK_STACK_KEY = "com.dimi.themoviedb.utils.BottomNavController.BackStack"
const val BOTTOM_NAV_PASSING_STACK_KEY =
    "com.dimi.themoviedb.utils.BottomNavController.PassingStack"

@FlowPreview
@ExperimentalCoroutinesApi
class BottomNavController(
    val context: Context,
    @IdRes val containerId: Int,
    @IdRes val appStartDestinationId: Int,
    val graphChangeListener: OnNavigationGraphChanged?,
    val navGraphProvider: NavGraphProvider
) {
    private val TAG: String = "AppDebug"
    lateinit var navigationBackStack: BackStack
    lateinit var passingBackStack: PassingStack
    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var navItemChangeListener: OnNavigationItemChanged
    var receivingNavigationStartFromFirstFragment: Boolean = false
    var lookingGraphId: Int = 0 // test

    init {
        if (context is Activity) {
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    fun setupBottomNavigationBackStack(previousBackStack: BackStack?) {
        navigationBackStack = previousBackStack?.let {
            it
        } ?: BackStack.of(appStartDestinationId)
    }

    fun setupBottomNavigationPassingStack(previousPassingStack: PassingStack?) {
        passingBackStack = previousPassingStack?.let {
            it
        } ?: PassingStack.of(null)
    }

    /**
     *
     * @param force -> force navigation programmatically so we do not save it in backStack
     *
     * @author Dimi
     */
    fun onNavigationItemSelected(
        itemId: Int = navigationBackStack.last(),
        force: Boolean = false,
        moveLast: Boolean = true
    ): Boolean {

        // Replace fragment representing a navigation item
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(containerId, fragment, itemId.toString())
            .addToBackStack(null)
            .commit()


        lookingGraphId = itemId // test

        // Update checked icon
        navItemChangeListener.onItemChanged(itemId)

        // communicate with Activity
        graphChangeListener?.onGraphChange(itemId)

        if (!force) {
            // Add to back stack
            if( moveLast ) navigationBackStack.moveLast(itemId)
            else navigationBackStack.add(itemId)
        }
        return true
    }

    fun setPassingObject(
        @IdRes comesFromGraphId: Int?,
        @IdRes goInGraphId: Int,
        passingModel: PassingModel,
        receivedObjectType: ReceivedObjectType
    ) {

        val passingObject =
            PassingObject(comesFromGraphId, goInGraphId, passingModel, receivedObjectType)

        if (!::passingBackStack.isInitialized)
            passingBackStack = PassingStack.of(passingObject)
        else
            passingBackStack.add(passingObject)

        onNavigationItemSelected(goInGraphId, true)
    }

    val passingObject: PassingObject?
        get() = passingBackStack.getLast()

    /**
     *
     * @param navController -> navController for navigation tab
     * @param reselect -> is fun called from "onBackPressed" or "onNavigationSelect"
     *
     * @return boolean if there is passed object in focused fragment
     * @author Dimi
     */
    @SuppressLint("RestrictedApi")
    fun checkIfThereIsPassedObject(
        navController: NavController,
        reselect: Boolean = true
    ): Boolean {

        passingObject?.let { passingObject ->

            // and myb if go in nav where is no object passed or passing return false myb isGraphWithPassedObject
            //if (!isGraphWithPassedObjectBoth(lookingGraphId)) return false

            // if onBack is pressed from from other navigation but not where is last passed object, jump on that page and then continue back
            if (lookingGraphId != passingObject.goInGraphId) {

                var returnValue = true
                if (navController.backStack.size > 2) {

                    for (i in passingBackStack.lastIndex downTo 0) {
                        if (passingBackStack[i].goInGraphId != lookingGraphId || passingBackStack[i].comesFromGraphId != lookingGraphId) {

                            returnValue = false
                            break
                        }
                    }
                }

                if (returnValue) onNavigationItemSelected(passingObject.goInGraphId!!, true)
                return returnValue
            }

            if (!reselect) {
                onNavigationItemSelected(passingObject.comesFromGraphId!!, true)
            }

            passingBackStack.removeLast()

            if (passingBackStack.isEmpty()) {
                if (receivingNavigationStartFromFirstFragment) {

                    navController.popBackStack()
                    receivingNavigationStartFromFirstFragment = false
                } else {
                    if (navController.backStack.size >= 4)
                        navController.popBackStack()
                }
            }

            return true
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    fun isGraphWithPassedObject(itemId: Int): Boolean {

        var returnValue = false
        passingObject?.let {
            for (i in passingBackStack.lastIndex downTo 0) {
                if (passingBackStack[i].goInGraphId == itemId) {
                    returnValue = true
                    break
                }
            }
        }
        return returnValue
    }


    @SuppressLint("RestrictedApi")
    fun onBackPressed() {

        val navController = fragmentManager.findFragmentById(containerId)!!
            .findNavController()

        when {
            navController.backStack.size > 2 -> {
                navController.currentDestination?.let { navDestination ->

                    if (navDestination.label == "ViewEpisodeFragment"|| navDestination.label == "ViewSeasonFragment" ) {
                        navController.popBackStack()
                    } else {
                        if (!checkIfThereIsPassedObject(navController, reselect = false)) {
                            navController.popBackStack()
                        } else {}
                    }
                }
//                if (!checkIfThereIsPassedObject(navController, reselect = false))
//                    navController.popBackStack()
            }

            // Fragment back stack is empty so try to go back on the navigation stack
            navigationBackStack.size > 1 -> {
                Log.d(TAG, "logInfo: BNC: backstack size > 1")

                // Remove last item from back stack
                navigationBackStack.removeLast()
                Log.d("Sve", "SASASA!4232323${navigationBackStack.size}")


                if (!checkIfThereIsPassedObject(navController, reselect = false)) {
                    // Update the container with new fragment
                    onNavigationItemSelected()
                }
            }
            // If the stack has only one and it's not the navigation home we should
            // ensure that the application always leave from startDestination
            navigationBackStack.last() != appStartDestinationId -> {
                Log.d(TAG, "logInfo: BNC: start != current")
                navigationBackStack.removeLast()
                navigationBackStack.add(0, appStartDestinationId)
                onNavigationItemSelected()
            }
            // Navigation stack is empty, so finish the activity
            else -> {
                Log.d(TAG, "logInfo: BNC: FINISH")
                activity.finish()
            }
        }
    }

    @Parcelize
    class PassingStack : ArrayList<PassingObject>(), Parcelable {

        companion object {

            fun of(vararg elements: PassingObject?): PassingStack {
                val b = PassingStack()

                for (item in elements)
                    item?.let { b.add(it) }

                return b
            }
        }

        fun isNavigationEmpty(itemId: Int): Boolean {
            var count = 0
            for (i in this.lastIndex downTo 0) {
                if (this[i].goInGraphId == itemId)
                    count++
            }
            return count == 0
        }

        fun getLast(): PassingObject? {
            if (lastIndex != -1) {
                return get(lastIndex)
            }
            return null
        }

        fun removeLast() = removeAt(size - 1)
    }

    @Parcelize
    class BackStack : ArrayList<Int>(), Parcelable {

        companion object {

            fun of(vararg elements: Int): BackStack {
                val b = BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(lastIndex)

        fun moveLast(item: Int) {
            remove(item) // if present, remove
            add(item) // add to end of list
        }
    }

    // For setting the checked icon in the bottom nav
    interface OnNavigationItemChanged {
        fun onItemChanged(itemId: Int)
    }

    // Get id of each graph
    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId: Int): Int
    }

    // Execute when Navigation Graph changes.
    interface OnNavigationGraphChanged {
        fun onGraphChange(itemId: Int)
    }

    interface OnNavigationReselectedListener {

        fun onReselectNavItem(navController: NavController, fragment: Fragment)
    }

    fun setOnItemNavigationChanged(listener: (itemId: Int) -> Unit) {
        this.navItemChangeListener = object : OnNavigationItemChanged {
            override fun onItemChanged(itemId: Int) {
                listener.invoke(itemId)
            }
        }
    }

}

/**
 * Using this data class to cover "jumping" from one navigation to another and send objects
 * etc. detecting click to see details in movie cast, that will jump to actor bottom navigation
 * @author Dimi
 */
data class PassingObject(
    @IdRes val comesFromGraphId: Int?,
    @IdRes val goInGraphId: Int?,
    val passingModel: PassingModel,
    val receivedObjectType: ReceivedObjectType
)

// types of objects that can be received
sealed class ReceivedObjectType {

    object Actor : ReceivedObjectType()

    object Movie : ReceivedObjectType()

    object TVShow : ReceivedObjectType()
}

// Convenience extension to set up the navigation
@ExperimentalCoroutinesApi
@FlowPreview
fun BottomNavigationView.setUpNavigation(
    bottomNavController: BottomNavController,
    onReselectListener: OnNavigationReselectedListener
) {

    setOnNavigationItemSelectedListener {

        /*
        * if user is going in another nav tab, check if there is a passed object and don't add to backStack but go there
        * */
        if (bottomNavController.isGraphWithPassedObject(it.itemId)) {
            Log.d("SVEA!@#", "DA!")
            bottomNavController.onNavigationItemSelected(it.itemId, true)
        } else {
            Log.d("SVEA!@#", "NE!")
            bottomNavController.onNavigationItemSelected(it.itemId, moveLast = false)
        }
    }

    setOnNavigationItemReselectedListener {

        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let { fragment ->

            onReselectListener.onReselectNavItem(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
        bottomNavController.onNavigationItemSelected()
    }

    bottomNavController.setOnItemNavigationChanged { itemId ->
        menu.findItem(itemId).isChecked = true
    }
}


/*
* class BottomNavController(
    val context: Context,
    @IdRes val containerId: Int,
    @IdRes val appStartDestinationId: Int,
    val graphChangeListener: OnNavigationGraphChanged?,
    val navGraphProvider: NavGraphProvider
) {
    private val TAG: String = "AppDebug"
    lateinit var navigationBackStack: BackStack
    lateinit var passingBackStack: PassingStack
    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var navItemChangeListener: OnNavigationItemChanged
    var receivingNavigationStartFromFirstFragment: Boolean = false
    var lookingGraphId: Int = 0 // test

    init {
        if (context is Activity) {
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    fun setupBottomNavigationBackStack(previousBackStack: BackStack?) {
        navigationBackStack = previousBackStack?.let {
            it
        } ?: BackStack.of(appStartDestinationId)
    }

    fun setupBottomNavigationPassingStack(previousPassingStack: PassingStack?) {
        passingBackStack = previousPassingStack?.let {
            it
        } ?: PassingStack.of(null)
    }

    /**
     *
     * @param force -> force navigation programmatically so we do not save it in backStack
     *
     * @author Dimi
     */
    fun onNavigationItemSelected(
        itemId: Int = navigationBackStack.last(),
        force: Boolean = false
    ): Boolean {

        // Replace fragment representing a navigation item
        val fragment = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getNavGraphId(itemId))
        fragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .replace(containerId, fragment, itemId.toString())
            .addToBackStack(null)
            .commit()


        lookingGraphId = itemId // test

        // Update checked icon
        navItemChangeListener.onItemChanged(itemId)

        // communicate with Activity
        graphChangeListener?.onGraphChange(itemId)

        if (!force) {

            // Add to back stack
            navigationBackStack.moveLast(itemId)
        }
        return true
    }

    fun setPassingObject(
        @IdRes comesFromGraphId: Int?,
        @IdRes goInGraphId: Int,
        passingModel: PassingModel,
        receivedObjectType: ReceivedObjectType
    ) {

        val passingObject =
            PassingObject(comesFromGraphId, goInGraphId, passingModel, receivedObjectType)

        if (!::passingBackStack.isInitialized)
            passingBackStack = PassingStack.of(passingObject)
        else
            passingBackStack.add(passingObject)

        Log.d("SVESASASASAS", passingModel.toString())

        onNavigationItemSelected(goInGraphId, true)
    }

    val passingObject: PassingObject?
        get() = passingBackStack.getLast()

    /**
     *
     * @param navController -> navController for navigation tab
     * @param reselect -> is fun called from "onBackPressed" or "onNavigationSelect"
     *
     * @return boolean if there is passed object in focused fragment
     * @author Dimi
     */
    @SuppressLint("RestrictedApi")
    fun checkIfThereIsPassedObject(
        navController: NavController,
        reselect: Boolean = true
    ): Boolean {

        passingObject?.let { passingObject ->

            // and myb if go in nav where is no object passed or passing return false myb isGraphWithPassedObject
            //if (!isGraphWithPassedObjectBoth(lookingGraphId)) return false

            // if onBack is pressed from from other navigation but not where is last passed object, jump on that page and then continue back
            if (lookingGraphId != passingObject.goInGraphId) {

                var returnValue = true
                if (navController.backStack.size > 2) {
                    for (i in passingBackStack.lastIndex downTo 0) {
                        if (passingBackStack[i].goInGraphId != lookingGraphId || passingBackStack[i].comesFromGraphId != lookingGraphId) {

                            returnValue = false
                            break
                        }
                    }
                }

                if (returnValue) onNavigationItemSelected(passingObject.goInGraphId!!, true)
                return returnValue
            }

            if (!reselect) {
                onNavigationItemSelected(passingObject.comesFromGraphId!!, true)
            }

            passingBackStack.removeLast()

            if (passingBackStack.isEmpty()) {
                if (receivingNavigationStartFromFirstFragment) {

                    navController.popBackStack()
                    receivingNavigationStartFromFirstFragment = false
                } else {
                    if (navController.backStack.size >= 4)
                        navController.popBackStack()
                }
            }

            return true
        }
        return false
    }

    @SuppressLint("RestrictedApi")
    fun isGraphWithPassedObject(itemId: Int): Boolean {

        var returnValue = false
        passingObject?.let {
            for (i in passingBackStack.lastIndex downTo 0) {
                if (passingBackStack[i].goInGraphId == itemId) {
                    returnValue = true
                    break
                }
            }
        }
        return returnValue
    }

    fun safeNavigate(@IdRes actionId: Int) {
        val navController = fragmentManager.findFragmentById(containerId)!!
            .findNavController()

        navController.navigate( actionId )
    }

    @SuppressLint("RestrictedApi")
    fun onBackPressed() {

        val navController = fragmentManager.findFragmentById(containerId)!!
            .findNavController()

        when {
            navController.backStack.size > 2 -> {
                if (!checkIfThereIsPassedObject(navController, reselect = false))
                    navController.popBackStack()
            }

            // Fragment back stack is empty so try to go back on the navigation stack
            navigationBackStack.size > 1 -> {
                Log.d(TAG, "logInfo: BNC: backstack size > 1")

                // Remove last item from back stack
                navigationBackStack.removeLast()


                if (!checkIfThereIsPassedObject(navController, reselect = false)) {

                    // Update the container with new fragment
                    onNavigationItemSelected()
                }
            }
            // If the stack has only one and it's not the navigation home we should
            // ensure that the application always leave from startDestination
            navigationBackStack.last() != appStartDestinationId -> {
                Log.d(TAG, "logInfo: BNC: start != current")
                navigationBackStack.removeLast()
                navigationBackStack.add(0, appStartDestinationId)
                onNavigationItemSelected()
            }
            // Navigation stack is empty, so finish the activity
            else -> {
                Log.d(TAG, "logInfo: BNC: FINISH")
                activity.finish()
            }
        }
    }

    @Parcelize
    class PassingStack : ArrayList<PassingObject>(), Parcelable {

        companion object {

            fun of(vararg elements: PassingObject?): PassingStack {
                val b = PassingStack()

                for (item in elements)
                    item?.let { b.add(it) }

                return b
            }
        }

        fun isNavigationEmpty(itemId: Int): Boolean {
            var count = 0
            for (i in this.lastIndex downTo 0) {
                if (this[i].goInGraphId == itemId)
                    count++
            }
            return count == 0
        }

        fun getLast(): PassingObject? {
            if (lastIndex != -1) {
                return get(lastIndex)
            }
            return null
        }

        fun removeLast() = removeAt(size - 1)
    }

    @Parcelize
    class BackStack : ArrayList<Int>(), Parcelable {

        companion object {

            fun of(vararg elements: Int): BackStack {
                val b = BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(size - 1)

        fun moveLast(item: Int) {
            remove(item) // if present, remove
            add(item) // add to end of list
        }
    }

    // For setting the checked icon in the bottom nav
    interface OnNavigationItemChanged {
        fun onItemChanged(itemId: Int)
    }

    // Get id of each graph
    interface NavGraphProvider {
        @NavigationRes
        fun getNavGraphId(itemId: Int): Int
    }

    // Execute when Navigation Graph changes.
    interface OnNavigationGraphChanged {
        fun onGraphChange(itemId: Int)
    }

    interface OnNavigationReselectedListener {

        fun onReselectNavItem(navController: NavController, fragment: Fragment)
    }

    fun setOnItemNavigationChanged(listener: (itemId: Int) -> Unit) {
        this.navItemChangeListener = object : OnNavigationItemChanged {
            override fun onItemChanged(itemId: Int) {
                listener.invoke(itemId)
            }
        }
    }

}

/**
 * Using this data class to cover "jumping" from one navigation to another and send objects
 * etc. detecting click to see details in movie cast, that will jump to actor bottom navigation
 * @author Dimi
 */
data class PassingObject(
    @IdRes val comesFromGraphId: Int?,
    @IdRes val goInGraphId: Int?,
    val passingModel: PassingModel,
    val receivedObjectType: ReceivedObjectType
)

// types of objects that can be received
sealed class ReceivedObjectType {

    object Actor : ReceivedObjectType()

    object Movie : ReceivedObjectType()

    object TVShow : ReceivedObjectType()
}

// Convenience extension to set up the navigation
fun BottomNavigationView.setUpNavigation(
    bottomNavController: BottomNavController,
    onReselectListener: OnNavigationReselectedListener
) {

    setOnNavigationItemSelectedListener {

        /*
        * if user is going in another nav tab, check if there is a passed object and don't add to backStack but go there
        * */
        if (bottomNavController.isGraphWithPassedObject(it.itemId)) {
            Log.d("AS", "logInfo: BNC:Da")
            bottomNavController.onNavigationItemSelected(it.itemId, true)
        } else {
            Log.d("AS", "logInfo: BNC:NE")
            bottomNavController.onNavigationItemSelected(it.itemId)
        }
    }

    setOnNavigationItemReselectedListener {

        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let { fragment ->

            onReselectListener.onReselectNavItem(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
        bottomNavController.onNavigationItemSelected()
    }

    bottomNavController.setOnItemNavigationChanged { itemId ->
        menu.findItem(itemId).isChecked = true
    }
}
*/



















