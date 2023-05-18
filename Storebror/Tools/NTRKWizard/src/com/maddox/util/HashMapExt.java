package com.maddox.util;

import java.io.Serializable;
import java.util.AbstractCollection;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public class HashMapExt extends AbstractMap implements Map, Cloneable, Serializable {
	private static final long serialVersionUID = 1L;
	private class HashIterator implements Iterator {

		public boolean hasNext() {
			return entry.lNext != tail;
		}

		public Object next() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			if (entry.lNext == tail) {
				throw new NoSuchElementException();
			} else {
				lastReturned = entry = entry.lNext;
				return type != 0 ? type != 1 ? ((Object)(entry)) : entry.value : entry.key;
			}
		}

		public void remove() {
			if (lastReturned == null)
				throw new IllegalStateException();
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
			HashMapExt.Entry aentry[] = table;
			int i = (lastReturned.hash & 0x7fffffff) % aentry.length;
			HashMapExt.Entry entry1 = aentry[i];
			HashMapExt.Entry entry2 = null;
			for (; entry1 != null; entry1 = entry1.next) {
				if (entry1 == lastReturned) {
					modCount++;
					expectedModCount++;
					if (entry2 == null)
						aentry[i] = entry1.next;
					else
						entry2.next = entry1.next;
					count--;
					lastReturned = null;
					entry = entry1.lPrev;
					freeEntry(entry1);
					return;
				}
				entry2 = entry1;
			}

			throw new ConcurrentModificationException();
		}

		HashMapExt.Entry entry;
		HashMapExt.Entry lastReturned;
		int type;
		private int expectedModCount;

		HashIterator(int i) {
			entry = head;
			lastReturned = null;
			expectedModCount = modCount;
			type = i;
		}
	}

	private static class Entry implements java.util.Map.Entry {

		protected Object clone() {
			return new Entry(hash, key, value, next != null ? (Entry)next.clone() : null);
		}

		public Object getKey() {
			return key;
		}

		public Object getValue() {
			return value;
		}

		public Object setValue(Object obj) {
			Object obj1 = value;
			value = obj;
			return obj1;
		}

		public boolean equals(Object obj) {
			if (!(obj instanceof java.util.Map.Entry)) {
				return false;
			} else {
				java.util.Map.Entry entry = (java.util.Map.Entry)obj;
				return (key != null ? key.equals(entry.getKey()) : entry.getKey() == null)
						&& (value != null ? value.equals(entry.getValue()) : entry.getValue() == null);
			}
		}

		public int hashCode() {
			return hash ^ (value != null ? value.hashCode() : 0);
		}

		public String toString() {
			return key.toString() + "=" + value.toString();
		}

		int hash;
		Object key;
		Object value;
		Entry next;
		Entry lNext;
		Entry lPrev;

		Entry(int i, Object obj, Object obj1, Entry entry) {
			hash = i;
			key = obj;
			value = obj1;
			next = entry;
		}
	}

	private HashMapExt(int i, float f) {
		modCount = 0;
		keySet = null;
		entrySet = null;
		values = null;
		freeEntryList = null;
		if (i < 0)
			throw new IllegalArgumentException("Illegal Initial Capacity: " + i);
		if (f <= 0.0F)
			throw new IllegalArgumentException("Illegal Load factor: " + f);
		if (i == 0)
			i = 1;
		loadFactor = f;
		table = new Entry[i];
		threshold = (int)((float)i * f);
		head = allocEntry(_entryHash++, null, null, null);
		tail = allocEntry(0, null, null, null);
		head.lNext = tail;
		tail.lPrev = head;
	}

	public HashMapExt() {
		this(101, 0.75F);
	}

	public int size() {
		return count;
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public int hashCode() {
		return head.hashCode();
	}

	public boolean equals(Object obj) {
		return this == obj;
	}

	public java.util.Map.Entry nextEntry(java.util.Map.Entry entry) {
		if (count == 0)
			return null;
		Entry entry1 = (Entry)entry;
		if (entry1 == null) {
			nextEntryLock = modCount;
			entry1 = head.lNext;
		} else {
			if (modCount != nextEntryLock)
				throw new ConcurrentModificationException();
			entry1 = entry1.lNext;
		}
		if (entry1 == tail)
			return null;
		else
			return entry1;
	}

	public boolean containsValue(Object obj) {
		Entry entry = head.lNext;
		if (obj == null) {
			while (entry != tail) {
				if (entry.value == null)
					return true;
				else
					entry = entry.next;
			}
		}
		while (entry != tail) {
			if (obj.equals(entry.value))
				return true;
			entry = entry.next;
		}
		return false;
	}

	public boolean containsKey(Object obj) {
		if (obj != null) {
			Entry aentry[] = table;
			int i = obj.hashCode();
			int j = (i & 0x7fffffff) % aentry.length;
			for (Entry entry1 = aentry[j]; entry1 != null; entry1 = entry1.next)
				if (entry1.hash == i && obj.equals(entry1.key))
					return true;

		} else {
			for (Entry entry = head.lNext; entry != tail; entry = entry.lNext)
				if (entry.key == null)
					return true;

		}
		return false;
	}

	public Object get(Object obj) {
		if (obj != null) {
			Entry aentry[] = table;
			int i = obj.hashCode();
			int j = (i & 0x7fffffff) % aentry.length;
			for (Entry entry1 = aentry[j]; entry1 != null; entry1 = entry1.next)
				if (entry1.hash == i && obj.equals(entry1.key))
					return entry1.value;

		} else {
			for (Entry entry = head.lNext; entry != tail; entry = entry.lNext)
				if (entry.key == null)
					return entry.value;

		}
		return null;
	}

	private void rehash() {
		int i = table.length * 2 + 1;
		Entry aentry[] = new Entry[i];
		modCount++;
		threshold = (int)((float)i * loadFactor);
		table = aentry;
		for (Entry entry = head.lNext; entry != tail; entry = entry.lNext) {
			int j = (entry.hash & 0x7fffffff) % i;
			entry.next = aentry[j];
			aentry[j] = entry;
		}

	}

	public Object put(Object obj, Object obj1) {
		Entry aentry[] = table;
		int i = 0;
		int j = 0;
		if (obj != null) {
			i = obj.hashCode();
			j = (i & 0x7fffffff) % aentry.length;
			for (Entry entry = aentry[j]; entry != null; entry = entry.next)
				if (entry.hash == i && obj.equals(entry.key)) {
					Object obj2 = entry.value;
					entry.value = obj1;
					return obj2;
				}

		} else {
			for (Entry entry1 = head.lNext; entry1 != tail; entry1 = entry1.lNext)
				if (entry1.key == null) {
					Object obj3 = entry1.value;
					entry1.value = obj1;
					return obj3;
				}

		}
		modCount++;
		if (count >= threshold) {
			rehash();
			aentry = table;
			j = (i & 0x7fffffff) % aentry.length;
		}
		Entry entry2 = allocEntry(i, obj, obj1, aentry[j]);
		entry2.lPrev = head;
		entry2.lNext = head.lNext;
		entry2.lNext.lPrev = entry2;
		head.lNext = entry2;
		aentry[j] = entry2;
		count++;
		return null;
	}

	public Object remove(Object obj) {
		Entry aentry[] = table;
		if (obj != null) {
			int i = obj.hashCode();
			int j = (i & 0x7fffffff) % aentry.length;
			Entry entry2 = aentry[j];
			Entry entry3 = null;
			for (; entry2 != null; entry2 = entry2.next) {
				if (entry2.hash == i && obj.equals(entry2.key)) {
					modCount++;
					if (entry3 != null)
						entry3.next = entry2.next;
					else
						aentry[j] = entry2.next;
					count--;
					Object obj2 = entry2.value;
					freeEntry(entry2);
					return obj2;
				}
				entry3 = entry2;
			}

		} else {
			Entry entry = aentry[0];
			Entry entry1 = null;
			for (; entry != null; entry = entry.next) {
				if (entry.key == null) {
					modCount++;
					if (entry1 != null)
						entry1.next = entry.next;
					else
						aentry[0] = entry.next;
					count--;
					Object obj1 = entry.value;
					freeEntry(entry);
					return obj1;
				}
				entry1 = entry;
			}

		}
		return null;
	}

	public void putAll(Map map) {
		java.util.Map.Entry entry;
		for (Iterator iterator = map.entrySet().iterator(); iterator.hasNext(); put(entry.getKey(), entry.getValue()))
			entry = (java.util.Map.Entry)iterator.next();

	}

	public void clear() {
		if (count == 0)
			return;
		modCount++;
		for (Entry entry = head.lNext; entry != tail;) {
			Entry entry1 = entry;
			entry = entry1.lNext;
			freeEntry(entry1);
		}

		Entry aentry[] = table;
		for (int i = aentry.length; --i >= 0;)
			aentry[i] = null;

		count = 0;
	}

	public Object clone() {
		try {
			HashMapExt hashmapext;
			hashmapext = (HashMapExt)super.clone();
			hashmapext.table = new Entry[table.length];
			for (int i = table.length; i-- > 0;) {
				for (Entry entry = hashmapext.table[i] = table[i] == null ? null : (Entry)table[i].clone(); entry != null; entry = entry.next) {
					entry.lPrev = hashmapext.head;
					entry.lNext = hashmapext.head.lNext;
					entry.lNext.lPrev = entry;
					hashmapext.head.lNext = entry;
				}

			}

			hashmapext.keySet = null;
			hashmapext.entrySet = null;
			hashmapext.values = null;
			hashmapext.modCount = 0;
			return hashmapext;
		} catch (Exception e) {
		}
		throw new InternalError();
	}

	public Set keySet() {
		if (keySet == null)
			keySet = new AbstractSet() {

			public Iterator iterator() {
					return new HashIterator(0);
				}

			public int size() {
					return count;
				}

			public boolean contains(Object obj) {
					return containsKey(obj);
				}

			public boolean remove(Object obj) {
					return HashMapExt.this.remove(obj) != null;
				}

			public void clear() {
					HashMapExt.this.clear();
				}

			};
		return keySet;
	}

	public Collection values() {
		if (values == null)
			values = new AbstractCollection() {

			public Iterator iterator() {
					return new HashIterator(1);
				}

			public int size() {
					return count;
				}

			public boolean contains(Object obj) {
					return containsValue(obj);
				}

			public void clear() {
					HashMapExt.this.clear();
				}

			};
		return values;
	}

	public Set entrySet() {
		if (entrySet == null)
			entrySet = new AbstractSet() {

			public Iterator iterator() {
					return new HashIterator(2);
				}

			public boolean contains(Object obj) {
					if (!(obj instanceof java.util.Map.Entry))
						return false;
					java.util.Map.Entry entry = (java.util.Map.Entry)obj;
					Object obj1 = entry.getKey();
					HashMapExt.Entry aentry[] = table;
					int i = obj1 != null ? obj1.hashCode() : 0;
					int j = (i & 0x7fffffff) % aentry.length;
					for (HashMapExt.Entry entry1 = aentry[j]; entry1 != null; entry1 = entry1.next)
						if (entry1.hash == i && entry1.equals(entry))
							return true;

					return false;
				}

			public boolean remove(Object obj) {
					if (!(obj instanceof java.util.Map.Entry))
						return false;
					java.util.Map.Entry entry = (java.util.Map.Entry)obj;
					Object obj1 = entry.getKey();
					HashMapExt.Entry aentry[] = table;
					int i = obj1 != null ? obj1.hashCode() : 0;
					int j = (i & 0x7fffffff) % aentry.length;
					HashMapExt.Entry entry1 = aentry[j];
					HashMapExt.Entry entry2 = null;
					for (; entry1 != null; entry1 = entry1.next) {
						if (entry1.hash == i && entry1.equals(entry)) {
							modCount++;
							if (entry2 != null)
								entry2.next = entry1.next;
							else
								aentry[j] = entry1.next;
							count--;
							freeEntry(entry1);
							return true;
						}
						entry2 = entry1;
					}

					return false;
				}

			public int size() {
					return count;
				}

			public void clear() {
					HashMapExt.this.clear();
				}

			};
		return entrySet;
	}

	private Entry allocEntry(int i, Object obj, Object obj1, Entry entry) {
		if (freeEntryList == null) {
			return new Entry(i, obj, obj1, entry);
		} else {
			Entry entry1 = freeEntryList;
			freeEntryList = freeEntryList.next;
			entry1.hash = i;
			entry1.key = obj;
			entry1.value = obj1;
			entry1.next = entry;
			return entry1;
		}
	}

	private void freeEntry(Entry entry) {
		entry.key = null;
		entry.value = null;
		entry.next = freeEntryList;
		freeEntryList = entry;
		entry.lPrev.lNext = entry.lNext;
		entry.lNext.lPrev = entry.lPrev;
		entry.lPrev = null;
		entry.lNext = null;
	}

	private transient Entry table[];
	private transient int count;
	private int threshold;
	private float loadFactor;
	private transient int modCount;
	private static int _entryHash = 0;
	private transient Entry head;
	private transient Entry tail;
	private transient int nextEntryLock;
	private transient Set keySet;
	private transient Set entrySet;
	private transient Collection values;
	private Entry freeEntryList;

}
