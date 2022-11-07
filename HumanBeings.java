package bibleprojecttwo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class HumanBeings {
	
	public long TotalPopulation = 0;
	public long TotalDisciples = 0;
	
	ArrayList<HumanGroup> NonBelievers;
	ArrayList<HumanGroup> Disciples;
	
	public HumanBeings(long population, int age) {
		this.NonBelievers = new ArrayList<HumanGroup>();
		HumanGroup firstGroup = new HumanGroup(population, age);
		this.NonBelievers.add(firstGroup);
		this.Disciples = new ArrayList<HumanGroup>();
	}
	
	public void Initiate(long initialDisciples, int age) {
		HumanGroup firstGroup = this.NonBelievers.get(0);
		firstGroup.Population -= initialDisciples;
		HumanGroup firstDisciples = new HumanGroup(initialDisciples, age);
		firstDisciples.IsDisciple = true;
		this.Disciples.add(firstDisciples);
		
		this.FindApprentice();
		this.Report();
	}
	
	public void FindApprentice() {
		long apprenticeCandNum = 0;
		for(HumanGroup hg : this.Disciples) {
			if (hg.CanTrainApprentice()) {
				long num  = hg.Population * Human.capOfApprentice;
				apprenticeCandNum += num;
			}
		}
		if (apprenticeCandNum == 0) {
			return;
		}
		Collections.sort(this.NonBelievers, HumanGroup.AgeComparator);
		Iterator<HumanGroup> itr = this.NonBelievers.iterator();
		HumanGroup potentialApprentice = null;
		while (itr.hasNext()) {
			HumanGroup hg = itr.next();
			if (hg.CanBeApprentice()) {
				if (hg.Population > apprenticeCandNum) {
					potentialApprentice = new HumanGroup(apprenticeCandNum, hg.Age);
					potentialApprentice.IsAprentice = true;
					hg.Population = hg.Population - apprenticeCandNum;
					break;
				} else { //less or equals
					hg.IsAprentice = true;
					if (hg.Population == apprenticeCandNum) {
						break;
					} else {
						apprenticeCandNum -= hg.Population;
					}
				}
			}
		}
		if (potentialApprentice != null) {
			this.NonBelievers.add(potentialApprentice);
		}
	}
	
	public void Report() {
		this.Count();
		double rate = (double)this.TotalDisciples / (double)this.TotalPopulation * (double)100;
		String percent = String. format("%.2f", rate);
		System.out.println("Total Disciples / Total Population: " + this.TotalDisciples + " / " + 
		this.TotalPopulation + " = " + percent + "%");
	}
	
	public void OneYearPass() {
		
		Iterator<HumanGroup> itr = this.NonBelievers.iterator();
		while (itr.hasNext()) {
			HumanGroup hg = itr.next();
			hg.Aging();
			
			if (hg.IsDead()) {
				itr.remove();
			}
		}
		Iterator<HumanGroup> discipleItr = this.Disciples.iterator();
		while (discipleItr.hasNext()) {
			HumanGroup hg = discipleItr.next();
			hg.Aging();
			
			if (hg.IsDead()) {
				discipleItr.remove();
			}
		}
		
		long babyTotal = 0;
		itr = this.NonBelievers.iterator();
		while (itr.hasNext()) {
			HumanGroup hg = itr.next();
			if (hg.IsGivingBirth()) {
				babyTotal += hg.Population / 2 * Human.capOfBaby;
			}
		}
		discipleItr = this.Disciples.iterator();
		while (discipleItr.hasNext()) {
			HumanGroup hg = discipleItr.next();
			if (hg.IsGivingBirth()) {
				babyTotal += hg.Population / 2 * Human.capOfBaby;
			}
		}
		if (babyTotal > 0) {
			HumanGroup babies = new HumanGroup(babyTotal, 0);
			this.NonBelievers.add(0, babies);
		}
		
		itr = this.NonBelievers.iterator();
		while (itr.hasNext()) {
			HumanGroup hg = itr.next();
			if (hg.HasBecomeDisciple()) {
				itr.remove();
				this.Disciples.add(hg);
			}
		}
		
		this.FindApprentice();
		this.Report();
	}
	
	public void Count() {
		this.TotalPopulation = 0;
		this.TotalDisciples = 0;
		for(HumanGroup hg : this.NonBelievers) {
			this.TotalPopulation += hg.Population;
		}
		for(HumanGroup hg : this.Disciples) {
			this.TotalDisciples += hg.Population;
		}
		this.TotalPopulation += this.TotalDisciples;
	}

}
