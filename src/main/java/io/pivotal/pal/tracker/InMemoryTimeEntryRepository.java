package io.pivotal.pal.tracker;

import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class InMemoryTimeEntryRepository implements TimeEntryRepository{
    Map<Long, TimeEntry> timeEntryRepoList = new HashMap();
    private int counter = 0;
    public TimeEntry create(TimeEntry timeEntry)
    {
        counter = counter+1;
        TimeEntry createdTimeEntry= new TimeEntry(counter, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
        timeEntryRepoList.put(Long.valueOf(counter), createdTimeEntry);
        return createdTimeEntry;
    }

    public TimeEntry find(long timeEntryId){
        return timeEntryRepoList.get(timeEntryId);
    }

    public List<TimeEntry> list(){
        Collection<TimeEntry> timeEntries = timeEntryRepoList.values();
        return timeEntries.stream().collect(toCollection(ArrayList::new));
    }

    public TimeEntry update(long timeEntryId, TimeEntry timeEntry){
        timeEntry.setId(timeEntryId);
        if(timeEntryRepoList.get(timeEntryId) != null) {
             timeEntryRepoList.put(timeEntryId, timeEntry);
             return timeEntryRepoList.get(timeEntryId);
        }
        else
            return null;
    }

    public void delete(long timeEntryId){
        timeEntryRepoList.remove(timeEntryId);
    }
}
