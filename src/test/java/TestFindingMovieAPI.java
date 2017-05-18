/*
 * Copyright - Copyright FindingMovie
 * Copyright (C) 2016 Jayamal Kulathunge. All Rights Reserved.
 *
 * Created Date: 9/5/16 8:07 AM
 * Last Modified Date: 9/4/16 3:12 PM
 * File: TestFindingMovieAPI
 *
 * This file is part of FindingMovie.
 *
 * FindingMovie is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FindingMovie is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Created by jayamal on 9/4/16.
 */
public class TestFindingMovieAPI {

    public static void main(String[] args) {

        String[] scheduleName = {"Schedule 1","Schedule 2","Schedule 3","Schedule 4","Schedule 5","Schedule 6","Schedule 7","Schedule 8"};
        String[] scheduleTime = {"09:10","11","14:25","17","20:30","20:30","20:30","21"};

        NavigableMap<String,List<String>> navigableMap=new TreeMap<String,List<String>>();

        for(int i=0;i<scheduleName.length;i++){

            if(navigableMap.containsKey(scheduleTime[i])){
                navigableMap.get(scheduleTime[i]).add(scheduleName[i]) ;
            } else {
                List<String> schedules = new ArrayList<String>();
                schedules.add(scheduleName[i]);
                navigableMap.put(scheduleTime[i], schedules);
            }

        }


        System.out.println("headMap " + navigableMap.headMap("20:30"));
        System.out.println("tailMap  " + navigableMap.tailMap("14:25"));
        System.out.println("subMap {current hour} " + navigableMap.subMap("20","21"));


    }


}
