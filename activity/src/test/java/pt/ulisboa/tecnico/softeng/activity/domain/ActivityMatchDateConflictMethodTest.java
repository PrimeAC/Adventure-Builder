package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by joaocarlos95 on 09/03/17.
 */
public class ActivityMatchDateConflictMethodTest {
    private ActivityOffer offer;
    private LocalDate begin;
    private LocalDate end;

    @Before
    public void setUp() {
        ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
        Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);
        this.begin = LocalDate.now();
        this.end = this.begin.plusDays(2);

        this.offer = new ActivityOffer(activity, begin, end);
    }

    //Test to verify if the dates given are the same as the initial offer dates
    @Test
    public void success() {
        Assert.assertTrue(this.offer.matchDateConflict(this.begin, this.end));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap01(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.minusDays(1), this.begin));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap02(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.minusDays(1), this.end.minusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap03(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.minusDays(1), this.end));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap04(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.minusDays(1), this.end.plusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap05(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin, this.end.minusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap06(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin, this.end.plusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap07(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.plusDays(1), this.end.minusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap08(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.plusDays(1), this.end));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap09(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.plusDays(1), this.end.plusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap10(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.plusDays(2), this.end.plusDays(1)));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap11(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin, this.begin));
    }

    //Test to verify if the dates given overlaps the dates of the initial offer
    @Test
    public void testDateOverlap12(){
        Assert.assertTrue(this.offer.matchDateConflict(this.begin.plusDays(2), this.end));
    }

    @After
    public void tearDown() {
        ActivityProvider.providers.clear();
    }

}
