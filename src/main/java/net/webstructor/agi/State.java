/*
 * MIT License
 * 
 * Copyright (c) 2005-2021 by Anton Kolonin, Aigents®
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package net.webstructor.agi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class State{
	HashMap<String,Integer> p;
	@Override
	public int hashCode() {
		//return p.hashCode();
		int code = 0;
		for (Integer v : p.values())
			code += v.hashCode();
		return code;
	}
	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof State))
			return false;
		State os = (State)other;
		//if (os.p.size() != p.size())
		//	return false;
		Set<String> keys = p.size() <= os.p.size() ? p.keySet() : os.p.keySet(); //TODO : intersection of the key sets?
		for (String key : keys) {
			if (!p.get(key).equals(os.p.get(key)))
				return false; 
		}
		return true;
	}
	public State() {
		p = new HashMap<String,Integer>();
	}
	public State(State other) {//deep restricted copy
		p = new HashMap<String,Integer>(other.p);
	}
	Integer value(String key) {
		return p.get(key);
	}
	Integer value(String key, Integer def) {
		Integer val = p.get(key);
		return val != null ? val : def;
	}
	State set(String key, Integer value) {
		p.put(key, value);
		return this;
	}
	State add(String key, Integer value) {
		Integer oldValue = p.get(key);
		if (oldValue != null)
			value += oldValue;
		p.put(key, value);
		return this;
	}
	void add(State other) {
		for (String key : other.p.keySet()) {
			Integer value = other.p.get(key); 
			Integer thisValue = p.get(key);
			p.put(key,thisValue != null ? thisValue + value : value);
		}
	}
	void add(State other, String[] keys) {
		for (String key : keys) {
			Integer value = other.p.get(key);
			if (value == null)
				continue;
			Integer thisValue = p.get(key);
			p.put(key,thisValue != null ? thisValue + value : value);
		}
	}
	boolean sameAs(State other,Set<String> feelings) {
		for (String key : feelings) {
			Integer value = other.p.get(key); 
			Integer thisValue = p.get(key);
			if (thisValue == null || !thisValue.equals(value))
				return false;
		}
		return true;
	}
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String key : p.keySet()) {
			if (sb.length() > 0)
				sb.append('\t');
			sb.append(key).append(':').append(p.get(key));
		}
		return sb.toString();
	}
	static Integer mostUsable(List<State> states, String key) {
		int max = 0;
		HashMap<Integer,Integer> options = new HashMap<Integer,Integer>();
		for (State s : states) {
			Integer value = s.p.get(key);
			Integer count = options.get(value);
			count = count == null ? 1 : count + 1;
			options.put(value, count);
			if (max < count)
				max = count;
		}
		for (Integer option : options.keySet()) {
			Integer count = options.get(option);
			if (count == max)
				return option;
		}
		return null;
	}
	int value(String key, int def) {
		Integer i = p.get(key);
		return i == null ? def : i;
	}
	public static Map<Integer,Integer> values(Set<State> states, String key) {
		Map<Integer,Integer> map = new HashMap<Integer,Integer>();
		for (State s : states) {
			Integer value = s.value(key);
			if (value != null) {
				Integer count = map.get(value);
				if (count == null)
					count = 0;
				map.put(value, new Integer(value + count));
			}
		}
		return map;
	}
	public static double distance(State a, State b, Map<String, Integer> ranges) {
		double sum2 = 0, count = 0; 
		Set<String> keys = a.p.size() <= a.p.size() ? a.p.keySet() : b.p.keySet(); //TODO : intersection of the key sets?
		for (String key : keys){
				int avalue = a.value(key);
				int bvalue = b.value(key);
				double v = bvalue - avalue;
				if (ranges != null) {
					Integer range = ranges.get(key);
					if (range != null)//hacky catchup
						v /= range;
				}
				sum2 += v*v;
				count += 1;
		}
		double distance = count ==  0 ? Double.MAX_VALUE : Math.sqrt(sum2/count);
		return distance;
	}
	public static double distance(State[] a, State[] b, Set<String> keys, Map<String,Integer> ranges) {
		double sum2 = 0, count = 0; 
		for (int i = 0; i < a.length; i++) {
			for (String key : keys){
				int avalue = a[i].value(key);
				int bvalue = b[i].value(key);
				double v = bvalue - avalue;
				if (ranges != null) {
					Integer range = ranges.get(key);
					if (range != null)//hacky catchup
						v /= range;
				}
				sum2 += v*v;
				count += 1;
			}
		}
		double distance = count ==  0 ? Double.MAX_VALUE : Math.sqrt(sum2/count);
		return distance;
	}
}
