package com.totoro.AntiAbuse.tools.async;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AsyncTaskSplitter<T> {

    /**
     * 멀티스레드 작업이 필요한 list 분할 유틸(작업수 = 총작업수/스레드수 균등분할)
     * @param list
     * @return
     */
    public List<List<T>> getSplitTaskList(List<T> list) {
        ArrayList<T> buffer = new ArrayList<>();
        List<List<T>> fList = new ArrayList<>();

        int seperatedCount = list.size()/ AsyncConfig.MAX_POOL_SIZE;
        int cnt = 0;
        //총 작업수보다 스레드풀 수가 큰 경우에는 스레드별로 작업을 1개씩 할당.
        if(isGreat(list.size(), AsyncConfig.MAX_POOL_SIZE)) {
            for(T row : list) {
                buffer.add(row);
                fList.add((List<T>) buffer.clone());
                buffer.clear();
            }
            return fList;
        }

        int count = 0;
        for(T row : list) {
            if(seperatedCount > count) {
                buffer.add(row);
                count++;
            } else if(seperatedCount == count) {
                fList.add((List<T>) buffer.clone());
                buffer.clear();
                buffer.add(row);
                count = 1;
            }
        }
        fList.add((List<T>) buffer.clone());
        buffer.clear();

        return fList;
    }

    /**
     * 멀티스레드 작업이 필요한 list 분할 유틸(작업수 지정)
     * 스레드수 이상의 작업수는 Queue에 대기
     * @param list
     * @param seperatedCount
     * @return
     */
    public List<List<T>> getSplitTaskList(List<T> list, int seperatedCount) {
        ArrayList<T> buffer = new ArrayList<>();
        List<List<T>> fList = new ArrayList<>();

        int totalCount = list.size();
        if(isGreat(totalCount, seperatedCount)) {
//            throw new Exception("seperatedCount greater than the totalCount");
        }

        //스레드수가 작업수보다 작으면
        if(isSmall((totalCount / seperatedCount), AsyncConfig.MAX_POOL_SIZE)) {
            //작업수에서 스레드수를 뺀 수가 큐에 남을 수량이고, 큐에 남을 수량보다 큐 사이즈가 작으면 오류!
            if(isSmall((totalCount/seperatedCount) - AsyncConfig.MAX_POOL_SIZE,  AsyncConfig.QUEUE_CAPACITY)) {
//                throw new BaseException("queue limit exceeded!!");
            }
        }

        int count = 0;
        for(T row : list) {
            if(seperatedCount > count) {
                buffer.add(row);
                count++;
            } else if(seperatedCount == count) {
                fList.add((List<T>) buffer.clone());
                buffer.clear();
                buffer.add(row);
                count = 1;
            }
        }
        fList.add((List<T>) buffer.clone());
        buffer.clear();

        return fList;
    }

    private static boolean isSmall(int baseCount, int compareCount) {
        return baseCount > compareCount;
    }

    private static boolean isGreat(int baseCount, int compareCount) {
        return baseCount < compareCount;
    }
}
