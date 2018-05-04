import 'package:watoplan/data/models.dart';

List<T> quicksort<T>(List<T> arr, int left, int right, SortCmp<T> cmp) {
  int idx = partition<T>(arr, left, right, cmp);
  if (left < idx - 1) quicksort(arr, left, idx - 1, cmp);
  if (right > idx) quicksort(arr, idx, right, cmp);
}

int partition<T>(List<T> arr, int left, int right, SortCmp<T> cmp) {
  T pivot = arr[((left + right) / 2).floor()];
  int i = left;
  int j = right;
  while (i <= j) {
    while (cmp(arr[i], pivot, true)) i++;
    while (cmp(arr[j], pivot, false)) j--;
    if (i <= j) {
      T tmp = arr[i];
      arr[i] = arr[j];
      arr[j] = tmp;
      i++;
      j--;
    }
  }
  return i;
}

typedef List<Activity> ActivitySort(List<Activity> activities);
typedef bool SortCmp<T>(T a, T b, bool dir);

class ActivitySorters {

  static List<Activity> byStartTime(List<Activity> activities) {
    List<Activity> newActivities = new List.from(activities);

    quicksort(
      newActivities,
      0, newActivities.length - 1,
      (a, b, dir) => dir
        ? a.data['start'].millisecondsSinceEpoch > b.data['start'].millisecondsSinceEpoch
        : a.data['start'].millisecondsSinceEpoch < b.data['start'].millisecondsSinceEpoch
    );

    return newActivities;
  }

  static List<Activity> byEndTime(List<Activity> activities) {
    List<Activity> newActivities = new List.from(activities);

    quicksort(
      newActivities,
      0, newActivities.length - 1,
      (a, b, dir) => dir
        ? a.data['end'].millisecondsSinceEpoch > b.data['end'].millisecondsSinceEpoch
        : a.data['end'].millisecondsSinceEpoch < b.data['end'].millisecondsSinceEpoch
    );

    return newActivities;
  }

  static List<Activity> byPriority(List<Activity> activities) {
    List<Activity> newActivities = new List.from(activities);

    return newActivities;
  }

  static List<Activity> byProgress(List<Activity> activities) {
    List<Activity> newActivities = new List.from(activities);

    return newActivities;
  }

  static List<Activity> byType(List<Activity> activities) {
    List<Activity> newActivities = new List.from(activities);

    return newActivities;
  }

}