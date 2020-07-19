// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import java.util.Collections;
import java.util.Stack;
import java.util.stream.Collectors;

public final class FindMeetingQuery {
	public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
		Collection<TimeRange> allRequestAttendeesTimeRanges = getRequestAttendeesTimeRanges(events, request);
		Collection<TimeRange> mergedAllRequestTimeRanges = getMergedRequestAttendessTimeRanges(allRequestAttendeesTimeRanges);
		return getPossibleTimeRanges(mergedAllRequestTimeRanges, request);
	}

	private Collection<TimeRange> getRequestAttendeesTimeRanges(Collection<Event> events, MeetingRequest request) {
		List<TimeRange> allRequestAttendeesTimeRanges = new ArrayList<>();
		for (Event event: events) {
			if (event.getAttendees().stream().filter(request.getAttendees()::contains).collect(Collectors.toList()).size() > 0) {
				allRequestAttendeesTimeRanges.add(event.getWhen());
			}
		}
		Collections.sort(allRequestAttendeesTimeRanges, TimeRange.ORDER_BY_START);
		return allRequestAttendeesTimeRanges;
	}

	private Collection<TimeRange> getMergedRequestAttendessTimeRanges(Collection<TimeRange> allRequestAttendeesTimeRanges) {
		Stack<TimeRange> stack = new Stack<>();
		for (TimeRange timeRange: allRequestAttendeesTimeRanges) {
			if (stack.isEmpty()) {
				stack.push(timeRange);
			} else {
				TimeRange top = stack.peek();
				if (!timeRange.overlaps(top))
					stack.push(timeRange);
				else {
					top = TimeRange.fromStartEnd(top.start(), Math.max(top.end(), timeRange.end()), false);
					stack.pop();
					stack.push(top);
				}
			}
		}
		ArrayList<TimeRange> mergedAllRequestTimeRanges = new ArrayList<>();
		while (!stack.isEmpty()) {
			mergedAllRequestTimeRanges.add(stack.peek());
			stack.pop();
		}
		Collections.reverse(mergedAllRequestTimeRanges);
		return mergedAllRequestTimeRanges;
	}

	private Collection<TimeRange> getPossibleTimeRanges(Collection<TimeRange> mergedAllRequestTimeRanges, MeetingRequest request) {
		int curr = TimeRange.START_OF_DAY;
		List<TimeRange> possibleTimeRanges = new ArrayList<TimeRange> ();
		for (TimeRange timeRange: mergedAllRequestTimeRanges) {
			if (timeRange.start() - curr >= (int) request.getDuration()) {
				possibleTimeRanges.add(TimeRange.fromStartEnd(curr, timeRange.start(), false));
			}
			curr = Math.max(curr, timeRange.end());
		}
		if (TimeRange.END_OF_DAY - curr >= (int) request.getDuration()) {
			possibleTimeRanges.add(TimeRange.fromStartEnd(curr, TimeRange.END_OF_DAY, true));
		}
		return possibleTimeRanges;
	}
}