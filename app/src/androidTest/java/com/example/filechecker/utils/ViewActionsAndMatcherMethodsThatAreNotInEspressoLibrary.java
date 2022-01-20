package com.example.filechecker.utils;


import static androidx.test.espresso.core.internal.deps.guava.base.Preconditions.checkNotNull;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.isA;

import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Checkable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.PerformException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.core.internal.deps.dagger.internal.Preconditions;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.espresso.util.HumanReadables;
import androidx.test.espresso.util.TreeIterables;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.concurrent.TimeoutException;

/**
 * Methods that were found to deal with {@link ViewAction} that are not included into Espresso.
 * Some of them are working, some of the are not working properly.
 * Methods that are not working properly are marked with @deprecated.
 * <p>
 * NB! It is good to have them here still to avoid repeatable researches over same methods over and over again.
 */
public class ViewActionsAndMatcherMethodsThatAreNotInEspressoLibrary {
    /**
     * Perform action of waiting for a specific view id.
     */
    public static ViewAction waitId(final int viewId, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isDisplayed();
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + viewId + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withId(viewId);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }

                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);

                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }

        };
    }

    /**
     * Perform action of waiting for a specific view with text.
     *
     * @deprecated during tests it does not work and could not find proper view
     */
    public static ViewAction waitText(final String text, final long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return withText(text);
            }

            @Override
            public String getDescription() {
                return "wait for a specific view with id <" + text + "> during " + millis + " millis.";
            }

            @Override
            public void perform(final UiController uiController, final View view) {
                uiController.loopMainThreadUntilIdle();
                final long startTime = System.currentTimeMillis();
                final long endTime = startTime + millis;
                final Matcher<View> viewMatcher = withText(text);

                do {
                    for (View child : TreeIterables.breadthFirstViewTraversal(view)) {
                        // found view with required ID
                        if (viewMatcher.matches(child)) {
                            return;
                        }
                    }
                    uiController.loopMainThreadForAtLeast(50);
                }
                while (System.currentTimeMillis() < endTime);
                // timeout happens
                throw new PerformException.Builder()
                        .withActionDescription(this.getDescription())
                        .withViewDescription(HumanReadables.describe(view))
                        .withCause(new TimeoutException())
                        .build();
            }

        };
    }

    /**
     * Shortcut to {@link RecyclerViewMatcher} constructor
     *
     * @param recyclerViewId id of the {@link RecyclerView}
     * @return matcher for provided {@link RecyclerView}
     */
    public static RecyclerViewMatcher withRecyclerView(final int recyclerViewId) {
        return new RecyclerViewMatcher(recyclerViewId);
    }

    /**
     * Same as withId(is(int)), but attempts to look up resource name of the given id and use an
     * R.id.myView style description with describeTo. If resource lookup is unavailable, at the time
     * describeTo is invoked, this will print out a simple “with id: %d”. If resource lookup is
     * available, but looking up the name for the given id, fails, “with id: %d (resource name not
     * found)” will be returned as the description.
     *
     * @param idOfView the resource id.
     * @deprecated Method does not work. Use {@link RecyclerViewMatcher#atPositionOnView(int, int)} instead.
     */
    @Deprecated
    public static Matcher<View> withRecyclerViewAndId(final int recyclerViewId, int position, int idOfView) {
        return new TypeSafeMatcher<View>() {
            Resources resources = null;

            @Override
            public void describeTo(Description description) {
                String idDescription = Integer.toString(idOfView);
                if (resources != null) {
                    try {
                        idDescription = resources.getResourceName(idOfView);
                    } catch (Resources.NotFoundException e) {
                        // No big deal, will just use the int value.
                        idDescription = String.format("%s (resource name not found)", idOfView);
                    }
                }
                description.appendText("with id: " + idDescription);
            }

            @Override
            public boolean matchesSafely(View view) {
                resources = view.getResources();

                boolean idTheSame = idOfView == view.getId();
                if (!idTheSame) {
                    return false;
                }

                View possibleRecyclerView = (View) view.getParent();
                boolean correctView = false;
                while (true) {
                    if (possibleRecyclerView == null) {
                        break;
                    }

                    if (possibleRecyclerView instanceof RecyclerView && possibleRecyclerView.getId() == recyclerViewId) {
                        correctView = true;
                        break;
                    }
                    possibleRecyclerView = (View) possibleRecyclerView.getParent();
                }

                if (correctView) {
                    RecyclerView rv = (RecyclerView) possibleRecyclerView;
                    RecyclerView.ViewHolder viewHolder = rv.findViewHolderForAdapterPosition(position);
                    // has no item on such position
                    return viewHolder != null && viewHolder.itemView.findViewById(idOfView) != null;
                }

                return false;
            }
        };
    }

    public static Matcher<View> atPosition(final int position, @NonNull final Matcher<View> itemMatcher) {
        checkNotNull(itemMatcher);
        return new BoundedMatcher<View, RecyclerView>(RecyclerView.class) {
            @Override
            public void describeTo(Description description) {
                description.appendText("has item at position " + position + ": ");
                itemMatcher.describeTo(description);
            }

            @Override
            protected boolean matchesSafely(final RecyclerView view) {
                RecyclerView.ViewHolder viewHolder = view.findViewHolderForAdapterPosition(position);
                if (viewHolder == null) {
                    // has no item on such position
                    return false;
                }
                return itemMatcher.matches(viewHolder.itemView);
            }
        };
    }

    public static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }

        };
    }

    public static Matcher<View> withChildViewCount(final int count, final Matcher<View> childMatcher) {
        return new BoundedMatcher<View, ViewGroup>(ViewGroup.class) {
            @Override
            protected boolean matchesSafely(ViewGroup viewGroup) {
                int matchCount = 0;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    if (childMatcher.matches(viewGroup.getChildAt(i))) {
                        matchCount++;
                    }
                }

                return matchCount == count;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("ViewGroup with child-count=" + count + " and");
                childMatcher.describeTo(description);
            }
        };
    }

    public static Matcher<View> nthChildOf(final Matcher<View> parentMatcher, final int childPosition) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + childPosition + " child view of type parentMatcher");
            }

            @Override
            public boolean matchesSafely(View view) {
                if (!(view.getParent() instanceof ViewGroup)) {
                    return parentMatcher.matches(view.getParent());
                }

                ViewGroup group = (ViewGroup) view.getParent();
                return parentMatcher.matches(view.getParent()) && group.getChildAt(childPosition).equals(view);
            }
        };
    }

    public static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            int currentIndex = 0;

            @Override
            public void describeTo(Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                return matcher.matches(view) && currentIndex++ == index;
            }
        };
    }

    public static ViewAction clickChildViewWithId(final int id) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                View v = view.findViewById(id);
                v.performClick();
            }
        };
    }

    public static ViewAction setChecked(final boolean checked) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return new Matcher<View>() {
                    @Override
                    public boolean matches(Object item) {
                        return isA(Checkable.class).matches(item);
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {
                    }

                    @Override
                    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
                    }

                    @Override
                    public void describeTo(Description description) {
                    }
                };
            }

            @Override
            public String getDescription() {
                return null;
            }

            @Override
            public void perform(UiController uiController, View view) {
                Checkable checkableView = (Checkable) view;
                checkableView.setChecked(checked);
            }
        };
    }

    public static final class WithClassNameMatcher extends TypeSafeMatcher<View> {

        final Matcher<String> classNameMatcher;

        private WithClassNameMatcher(Matcher<String> classNameMatcher) {
            this.classNameMatcher = classNameMatcher;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("with class name: ");
            classNameMatcher.describeTo(description);
        }

        @Override
        public boolean matchesSafely(View view) {
            return classNameMatcher.matches(view.getClass().getSimpleName());
        }
    }

    /**
     * Returns a matcher that matches Views with class simple name matching the given matcher.
     */
    public static Matcher<View> withClassSimpleName(final Matcher<String> classNameMatcher) {
        return new WithClassNameMatcher(Preconditions.checkNotNull(classNameMatcher));
    }
}

