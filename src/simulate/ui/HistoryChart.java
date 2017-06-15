package simulate.ui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.RectangularShape;
import java.util.Date;
import java.util.Vector;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.labels.XYToolTipGenerator;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.ohlc.OHLCItem;
import org.jfree.data.time.ohlc.OHLCSeries;
import org.jfree.data.time.ohlc.OHLCSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleEdge;

import common.ConfigConst;
import simulate.data.SimuPriceSeries;
import simulate.pojo.DataMkt;
import util.FormatTool;

public class HistoryChart extends ChartPanel implements Runnable {

	private static final long serialVersionUID = 1L;
			
	public static TimeSeries priceSeries;
	private static TimeSeriesCollection priceCollection;
	
	public static TimeSeries volumeSeries;
	private static TimeSeriesCollection volumeCollection;
	
	public static OHLCSeries ohlcSeries;
	private static OHLCSeriesCollection ohlcCollection;
	
	private boolean domainCrosshairState = false;
	private boolean isDraged = false;
	private Vector<Point> draglist = new Vector<Point>();
	private static DataMkt storage = DataMkt.instance;
	
	public static Date sysLowerDate = new Date(FormatTool.getTodayBasedTime (ConfigConst.TWS_AM_START));
	public static Date sysUpperDate = new Date(FormatTool.getTodayBasedTime (ConfigConst.TWS_PM_END));

	public HistoryChart() {
		super(createChart());
	}
	
	private static JFreeChart createChart(){
		//domain yield is the same: by second
		DateAxis dateAxis = new DateAxis("Time");
	    dateAxis.setDateFormatOverride(FormatTool.getHmsTimeFormat());
        dateAxis.setAutoTickUnitSelection(true);
        dateAxis.setFixedAutoRange(60*60*1000*ConfigConst.SCREEN_SHOW_TIMESPAN);
        dateAxis.setLowerMargin(0);
        dateAxis.setUpperMargin(0);
        dateAxis.setPositiveArrowVisible(true);
        //the price axis
		priceSeries = new TimeSeries("Price");
		priceCollection = new TimeSeriesCollection();
		priceCollection.addSeries(priceSeries);
		NumberAxis priceAxis = new NumberAxis("Price");
        priceAxis.setAutoRange(true);
        priceAxis.setAutoRangeIncludesZero(false);
        priceAxis.setPositiveArrowVisible(true);
        priceAxis.setLabelPaint(Color.magenta);
        priceAxis.setTickLabelPaint(Color.magenta);
        //number renderer
        StandardXYItemRenderer priceRender = new StandardXYItemRenderer();
        priceRender.setSeriesPaint(0, Color.magenta); 
        priceRender.setBaseToolTipGenerator(new XYToolTipGenerator() {
			@Override
			public String generateToolTip(XYDataset priceSerials, int series, int item) {
				return String.format("Time = %s ; Price = %s", FormatTool.getHMS((long)priceSerials.getXValue(series, item)), 
						FormatTool.parseDouble2Str(priceSerials.getYValue(series, item)));
			}
		});
        priceRender.setBaseShapesFilled(true);
        XYPlot pricePlot = new XYPlot(priceCollection, dateAxis, priceAxis, priceRender);
		pricePlot.setDomainGridlinesVisible(false);
		
		//the volume axis
		volumeSeries = new TimeSeries("Volume");
		volumeCollection = new TimeSeriesCollection();
		volumeCollection.addSeries(volumeSeries);
		NumberAxis volumeAxis = new NumberAxis("Volume");
        volumeAxis.setAutoRange(true);
        volumeAxis.setAutoRangeIncludesZero(false);
        volumeAxis.setPositiveArrowVisible(true);
        volumeAxis.setLabelPaint(Color.red);
        volumeAxis.setTickLabelPaint(Color.red);
        //bar renderer
        XYBarRenderer volumeRender = new XYBarRenderer(){
			private static final long serialVersionUID = 1L;
			public Paint getItemPaint(int series, int item) {
				int spanstart = item * ConfigConst.CANDLESTICK_TIMESPAN;
				int spanend = (item + 1) * ConfigConst.CANDLESTICK_TIMESPAN - 1;
                if (storage.getHis(spanend).close >= storage.getHis(spanstart).close) 
                    return Color.green;
                else return Color.red;
            }
        };
        volumeRender.setDrawBarOutline(false);
        volumeRender.setShadowVisible(false);
        volumeRender.setBarPainter(new StandardXYBarPainter(){
			private static final long serialVersionUID = -2549690181037214212L;
			public void paintBar(Graphics2D g2, XYBarRenderer renderer, int row,
                    int column, RectangularShape bar, RectangleEdge base) {
        		Paint itemPaint = renderer.getItemPaint(row, column);
                g2.setPaint(itemPaint);
                bar.setFrameFromCenter(bar.getCenterX(), bar.getCenterY(), 
                		bar.getCenterX() + Math.max(0.5 * Math.pow(ConfigConst.CANDLESTICK_TIMESPAN, 2)/(ConfigConst.SCREEN_SHOW_TIMESPAN*60*60) , 1), 
                		bar.getCenterY() + bar.getHeight()/2);
                g2.fill(bar);
        	}
        });
        
        volumeRender.setBaseToolTipGenerator(new StandardXYToolTipGenerator("Time = {1} ; Volume= {2}", FormatTool.getHmsTimeFormat(), FormatTool.getIntFormat()));
        XYPlot volumePlot = new XYPlot(volumeCollection, dateAxis, volumeAxis, volumeRender);
        volumePlot.setDomainGridlinesVisible(false); 
		
        //the ohlc candas axis
		ohlcSeries = new OHLCSeries("KChart");
		ohlcCollection = new OHLCSeriesCollection();
		ohlcCollection.addSeries(ohlcSeries);
		//ohlc axis
		NumberAxis candleAxis = new NumberAxis("KChart");
		candleAxis.setAutoRange(true);
		candleAxis.setAutoRangeIncludesZero(false);
		candleAxis.setPositiveArrowVisible(true);
		candleAxis.setLabelPaint(Color.orange);
		candleAxis.setTickLabelPaint(Color.orange);
		CandlestickRenderer candleRenderer = new CandlestickRenderer();
		candleRenderer.setUpPaint(Color.green);
		candleRenderer.setDownPaint(Color.red);
		candleRenderer.setSeriesPaint(0, Color.orange); 
		candleRenderer.setAutoWidthMethod(CandlestickRenderer.WIDTHMETHOD_SMALLEST);
		candleRenderer.setBaseToolTipGenerator(new XYToolTipGenerator() {
			@Override
			public String generateToolTip(XYDataset paramXYDataset, int series, int itemindex) {
				OHLCItem item = (OHLCItem) ohlcSeries.getDataItem(itemindex);
				return String.format("Time = %s ; Open = %s , High = %s , Low = %s , Close = %s", 
						FormatTool.getHMS((long)item.getPeriod().getFirstMillisecond()), 
						FormatTool.parseDouble2Str(item.getOpenValue()), 
						FormatTool.parseDouble2Str(item.getHighValue()), 
						FormatTool.parseDouble2Str(item.getLowValue()), 
						FormatTool.parseDouble2Str(item.getCloseValue())
						);
			}
		});
		XYPlot candlePlot = new XYPlot(ohlcCollection, dateAxis, candleAxis, candleRenderer);
		candlePlot.setDomainGridlinesVisible(false); 
	    
        CombinedDomainXYPlot domainXYPlot = new CombinedDomainXYPlot(dateAxis);
        domainXYPlot.add(pricePlot, 1);
        domainXYPlot.add(volumePlot, 1);
        domainXYPlot.add(candlePlot, 1);
        
        JFreeChart freeChart = new JFreeChart("Futures", domainXYPlot);
		freeChart.addSubtitle(new TextTitle("Price,Volume,K-Chart"));
        return freeChart;
	}
	
	@Override
	public void run() {  
		SimuPriceSeries series = new SimuPriceSeries();
		series.execute();
	}
	
	@Override
	public void mouseClicked(MouseEvent event) {
		super.mouseClicked(event);
		ChartPanel chartPanel = (ChartPanel) event.getComponent();
		if(event.getClickCount() == 1){
	         domainCrosshairState  = !domainCrosshairState;
	         chartPanel.setHorizontalAxisTrace(domainCrosshairState); 
	         chartPanel.setVerticalAxisTrace(domainCrosshairState);
	      }
	}
	
	//查看前段时间的数据，单位为hour
	private void showSpanData(double timespan){
		DateAxis dateAxis = (DateAxis) this.getChart().getXYPlot().getDomainAxis();
		Date start, end;
		if(timespan > 0){
			//即拖拽的最后位置A1在初始点A0的右侧，即x1-x0>0
			//整个时间轴往右平移timespan个单位
			start = FormatTool.getRelativeTime(dateAxis.getMinimumDate(), timespan, 0, 0);
			end = FormatTool.getRelativeTime(dateAxis.getMaximumDate(), timespan, 0, 0);
		}else{
			//即拖拽的最后位置A1在初始点A0的左侧，即x1-x0<0
			//整个时间轴往左平移timespan个单位
			start = FormatTool.getRelativeTime(dateAxis.getMinimumDate(), timespan, 0, 0);
			end = FormatTool.getRelativeTime(dateAxis.getMaximumDate(), timespan, 0, 0);
		}
		if(sysLowerDate.after(start)) {
			start = sysLowerDate;
			end = FormatTool.getRelativeTime(start, ConfigConst.SCREEN_SHOW_TIMESPAN, 0, 0);
		}
//		if(sysUpperDate.before(end)) {
//			end = sysUpperDate;
//			start = FormatTool.getRelativeTime(end, -ConfigConst.SCREEN_SHOW_TIMESPAN, 0, 0);
//		}
		dateAxis.setMinimumDate(start);
		dateAxis.setMaximumDate(end);
		ohlcSeries.setNotify(true);
    	ohlcSeries.fireSeriesChanged();
    	volumeSeries.setNotify(true);
    	volumeSeries.fireSeriesChanged();
    	priceSeries.setNotify(true);
    	priceSeries.fireSeriesChanged();
	}	
	
	@Override
	public void mouseReleased(MouseEvent event) {
		if(isDraged){
			double percent = (event.getX() - draglist.get(0).getX())/this.getWidth();
			this.showSpanData(ConfigConst.TWS_ST_SPAN * percent);
			isDraged = false;
			draglist = new Vector<Point>();
		}else{
			this.restoreAutoBounds();
		}
	}
	
	@Override
	public void mouseDragged(MouseEvent event) {
		draglist.add(new Point(event.getX(),event.getY()));
		isDraged = true;
	}
	
	public void modifyWindow(){
		DateAxis dateAxis = (DateAxis) this.getChart().getXYPlot().getDomainAxis();
		dateAxis.setFixedAutoRange(60*60*1000*ConfigConst.SCREEN_SHOW_TIMESPAN);
		this.restoreAutoBounds();
	}
}
