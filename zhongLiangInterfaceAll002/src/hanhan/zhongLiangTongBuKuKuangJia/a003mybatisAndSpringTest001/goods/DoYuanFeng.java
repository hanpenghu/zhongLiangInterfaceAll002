package hanhan.zhongLiangTongBuKuKuangJia.a003mybatisAndSpringTest001.goods;
/*2016/12/13,更新:在商品同步的时候把中粮的goodsid插入到商品的productID
 * 并且给后面的图片Thumbnailun40加图片,加的还是中粮的第一个图片path1
 * 给海商首页那个编号是220的图片加图,加的也是中粮的path1*/

/*注意,删Hishop_products 库的时候  规格表会被同时删掉,因为是关联的
 * */
/**
 * 中粮库存也是通过name关联的
 * */
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;

import hanhan.Impl.GoodsInfo;
import hanhan.Impl.GoodsInfoToEntity;
import hanhan.entity.MenDianTabEntity.Hishop_StoreStock;
import hanhan.entity.MenDianTabEntity.Hishop_Stores;
import hanhan.entity.goodsEntity.GoodsPool;
import hanhan.entity.goodsEntity.GoodsPoolList;
import hanhan.entity.goodsEntity.Hishop_Categories.Hishop_Categories;
import hanhan.entity.goodsEntity.Hishop_Products.Hishop_Products;
import hanhan.entity.goodsEntity.goodsComments.CommentsObj;
import hanhan.entity.goodsEntity.goodsComments.CommentsObjList;
import hanhan.entity.goodsEntity.goodsDetail.GoodsDetail;
import hanhan.entity.goodsEntity.goodsDetail.GoodsDetailList;
import hanhan.entity.goodsEntity.goodsID.GoodsID;
import hanhan.entity.goodsEntity.goodsID.GoodsIDList;
import hanhan.entity.goodsEntity.goodsKuCun.KuCunDetail;
import hanhan.entity.goodsEntity.goodsKuCun.KuCunList;
import hanhan.entity.goodsEntity.goodsKuCun.kuCunRuCan.KuCunRuCan;
import hanhan.entity.goodsEntity.goodsKuCun.kuCunRuCan.KuCunRuCanList;
import hanhan.entity.goodsEntity.goodsPrice.GoodsPriceDetail;
import hanhan.entity.goodsEntity.goodsPrice.GoodsPriceList;
import hanhan.entity.goodsEntity.goodsUp1AndDown0status.GoodsUp1AndDown0Status;
import hanhan.entity.goodsEntity.goodsUp1AndDown0status.GoodsUp1AndDown0StatusList;
import hanhan.entity.goodsEntity.imageDetail.ImageDetailObj;
import hanhan.entity.goodsEntity.imageDetail.ImageDetailObjList;
import hanhan.entity.goodsEntity.imageDetail.ImageObj;
import hanhan.entity.goodsEntity.imageEtity.shang_pin_tu_pian;
import hanhan.entity.goodsEntity.suoYouShangPinZai31GeQuYuDeFenBieKuCun.*;
import hanhan.entity.shangPinGuiGeXiangXiBiao.Hishop_SKUs;
import hanhan.utils.com.hanhan.CutList;
import hanhan.utils.com.hanhan.FenYe;
import hanhan.utils.com.hanhan.sqlBatch.BatchGoodsDetailTemp;
import hanhan.utils.com.hanhan.sqlBatch.BatchGoodsId;
import hanhan.utils.com.hanhan.sqlBatch.BatchGoodsPool;
import hanhan.utils.com.hanhan.sqlBatch.BatchGoodsSaleStatus;
import hanhan.utils.com.hanhan.sqlBatch.BatchImageDetail;
import hanhan.utils.com.hanhan.sqlBatch.BatchTempPrice;
import hanhan.utils.com.hanhan.toDBbuShiYongPatch.CategoryExcelToDb;
import hanhan.utilsOfMd5_32AndPostAPI.pageBean.PageBeanGeneralOfSqlServer;
import hanhan.utilsOfMd5_32AndPostAPI.pageBean.PageBeanOfHishop_SKUs;
import hanhan.utilsOfMd5_32AndPostAPI.springContextUtiils.SpringContextHolder;
import hanhan.zhongLiangTongBuKuKuangJia.a002IterfacMapper.ICRUDmanDB;

//////////////////////////////////////////////////////////////////////////////
@Component("doYuanFeng")
public class DoYuanFeng {
	Logger log = LogManager.getLogger(DoYuanFeng.class.getName());
	@Autowired
	private ICRUDmanDB iCRUDmanDB;

	// ////////////////////////////////////////////////////////////////////////////
	public DoYuanFeng() {}// 无参构造
	// //////////////////区域仓库对照表建立//////////////////////////////////////////////////////////

	public List<KuCunRuCan> f1ShangPinQuYuDuiZhaoList() {// 从中粮得到商品区域对照list
		try {
			// 从properties读取所有区域仓库对照表到String
			String allAreaAndWarehouseInfo = new GoodsInfo()
					.getAllAreaAndWarehouseInfo();
			KuCunRuCanList KuCunRuCanList = JSON.parseObject(
					allAreaAndWarehouseInfo, KuCunRuCanList.class);
			List<KuCunRuCan> areaAndWarehouseList = KuCunRuCanList.getAreaAndWarehouse();
			return areaAndWarehouseList;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	public void quYuCangKuDuiZhaoBiao() {// 同步中粮的商品区域对照表(该表是直接给在页面的31个3值对)到数据库
		try {
			List<KuCunRuCan> areaAndWarehouseList = f1ShangPinQuYuDuiZhaoList();
			iCRUDmanDB.insertToQuYuCangKuDuiZhaoBiao(areaAndWarehouseList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	// //////这个方法虽然嫩赶得到所有商品在所有地区的库存同步到库(库还没建立),但是不能让库存跟中粮同步,这样是不行的/耗时太长///////////////所有商品在31个区域的分别库存///没有使用自建数据库的地区仓库对照表,而是使用的properties中的JSON///////////////////////////////////////////////////
	/*
	 * public void suoYouShangPinZai31GeQuYuDeKuCunLinShiBiao( ){
	 * //得到区域仓库对照的对象集合
	 * //通过/zhongLiangInterfaceAll/src/hanhan/properties/areaAndWarehouse
	 * /areaAndWarehouse.properties得到 List<KuCunRuCan> areaAndWarehouseList =
	 * f1ShangPinQuYuDuiZhaoList(); //该区域仓库对照实际上31个,我们按照区域来疯转库存并存入数据库临时表:
	 * 区域库存对照表//实际上作废这个list // List<ProductAreaInventory>list=new
	 * ArrayList<ProductAreaInventory>( ); //得到所有商品id//中粮估计有10000条 List<GoodsID>
	 * goodsIDList = iCRUDmanDB.queryAllGoodsID();
	 * 
	 * //类KuCunRuCan是库存入参是从/zhongLiangInterfaceAll/src/hanhan/properties/
	 * areaAndWarehouse/areaAndWarehouse.properties得到的3值键 for(KuCunRuCan
	 * KCRC:areaAndWarehouseList){//循环31个省地域 //循环一万条商品id for(GoodsID
	 * GoodsID:goodsIDList){//循环临时id库存表suo_you_shang_pin_id_lin_shi_ku try { //
	 * long time1 = new Date( ).getTime(); //准备封装区域和商品id和库存对照的类 // public class
	 * ProductAreaInventory { // public ProductAreaInventory() {} // String
	 * areaID; // String areaName; // String areaInventory; // String productID;
	 * ProductAreaInventory ProductAreaInventory=new ProductAreaInventory( );
	 * String areaID = KCRC.getAreaID();//地区id其实来自读取的properties文件
	 * ProductAreaInventory.setAreaID(areaID);
	 * ProductAreaInventory.setAreaName(KCRC
	 * .getAreaName());//地区name来自读取的properties
	 * //得到当前循环的id并放入ProductAreaInventory商品区域库存对照类 String itemid =
	 * GoodsID.getItemid().trim( );
	 * ProductAreaInventory.setProductID(itemid);//产品id来自产品id临时表
	 * //实际上得到的只有一个元素的集合,因为我一次只是传入一个id//得到某个区域某个商品id的库存并封装成实体并封进list // public
	 * class KuCunDetail {//封装官方的出参实例 // private String skuid; // private String
	 * inventory; /////////////////////////////////////////// // public class
	 * KuCunList { // private List<KuCunDetail> Inventory = new
	 * ArrayList<KuCunDetail>(); KuCunList KuCunList= new GoodsInfoToEntity(
	 * ).getInventoryInfoWithProvinceIdAndSkuidsToEntity(areaID,new
	 * String[]{itemid}); //给区域仓库数量对照表设置区域库存数量
	 * ProductAreaInventory.setAreaInventory
	 * (KuCunList.getInventory().get(0).getInventory()); //
	 * list.add(ProductAreaInventory); // System.out.println(list);
	 * //注意上面那种做法非常危险,把31万条数据一次放入内存再处理,那么我们就拿到一条插入一条喽
	 * iCRUDmanDB.insertToInventory_area_Tab(ProductAreaInventory); long time2 =
	 * new Date( ).getTime(); //
	 * System.out.println("耗用时间++"+(time2-time1)/1000+"秒"); } catch (Exception
	 * e) {e.printStackTrace();} } }
	 * 
	 * // System.out.println(list); }
	 */
	// ///////////////////////////////////1,商品池页码名字临时库/////////////////////////////////////////
	// @Scheduled(fixedRate=2000)//第一次任务执行完后每隔2秒执行一次
	// @Scheduled(cron ="0 50 18 * * ?")//在第一次任务执行后每天18点50运行一次
	public void tongBuShangPinChiYeMaHeMingZi() {
		log.debug("-----------------商品池页面名字临时库建立开始-----------------");
		System.out.println("商品池页面名字临时库建立开始");
		try {
			// 先删除所有商品页码
			System.out.println(iCRUDmanDB.deleteAllShangPinYeMaBiao());
			// 得到所有商品池的list,准备放入表中
			GoodsPoolList goodsPoolList = new GoodsInfoToEntity()
					.getAllGoodsPoolIdToEntity();
			List<GoodsPool> goodPoolList = goodsPoolList.getItempagenum();
			// 在mapper中批处理
//			iCRUDmanDB.insertShangPinChiHeYeMa(goodPoolList);
			new BatchGoodsPool( ).save(goodPoolList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug("-----------------商品池页面名字临时库建立-结束----------------");
		System.out.println("商品池页面名字临时库建立结束");
	}

	// ////////////////////////////////////////////////////////////////////////////

	// ///////////////////////////////2,商品编goodsId号临时库/////////////////////////////////////////////
	public void tongBuSuoYouShangPinBianHao() {
		System.out.println("商品标号临时库建立开始");
		BatchGoodsId batchGoodsId = new BatchGoodsId( );
		try {
			// 先删除上次所有编号
			iCRUDmanDB.deleteSuoYouShangPinId();// 先删除上次所有编号
			List<GoodsPool> goodPoolList = iCRUDmanDB.selectAllShangPinYeMa();
			for (GoodsPool goodsPool : goodPoolList) {
				try {
					// 得到页码
					int page_num = goodsPool.getPage_num();
					// 得到商品编号
					GoodsIDList goodsIDList = new GoodsInfoToEntity()
							.getAllGoodsIdsWithPageNumToEntity(String
									.valueOf(page_num));
					List<GoodsID> itemlist = goodsIDList.getItemlist();// 当前页所有商品编号
					//平分itemlist,每个里面有40个//不够的情况已经处理
//					List<List<GoodsID>> cutList1 = new CutList<GoodsID>( ).cutList1(itemlist, 40);
					/*for(List<GoodsID> listX:cutList1){
						iCRUDmanDB.insertLinShiShangPinIdKu(listX);
					}*/
//					iCRUDmanDB.insertLinShiShangPinIdKu(itemlist);
					batchGoodsId.save(itemlist);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug("-----------------商品标号临时库建立结束-结束----------------");
		System.out.println("商品标号临时库建立结束");
	}

	// ////////////////////////////////3,商品详情临时库////////////////////////////////////////////
//萌逼了,经本人测试,中粮接口中的详情只能一次拿出一条,这样就不能批处理了
	public void tongBuSuoYouShangPinXiangQing() {
		System.out.println("商品详情临时库建立开始");
		BatchGoodsDetailTemp batchGoodsDetailTemp = new BatchGoodsDetailTemp();
		log.debug("-----------------商品详情临时库建立开始-----------------");
		try {
			// 先删除所有商品详情
			System.out.println(iCRUDmanDB.deleteAllgoodsDetail());
			// 分页,查询商品在中粮的ID进行操作
			// 统计总页数
			Integer queryAllGoosIDCount = iCRUDmanDB.queryAllGoosIDCount();
			FenYe fy=new FenYe( );
			fy.setZongJiLuShu(iCRUDmanDB.queryAllGoosIDCount());
			fy.setMeiYeXianShiShu(50);
			// 得到总页数
			fy.setZongYeShu();
			// 通过总页数进行循环//操作一页插入数据库一页
			for (int n = 0; n < fy.getZongYeShu(); ++n) {
				log.debug("-----------------商品详情临时库第"+(n+1)+"页建立开始-----------------");
				try {
					fy.setDangQianYe(n+1);
					
					List<GoodsID> goodsIDList = iCRUDmanDB.querySomeGoodsID(fy.getDangQianYe(),fy.getMeiYeXianShiShu());
					
					List<GoodsDetail> list = new ArrayList<GoodsDetail>();
					long time1 = new Date( ).getTime();
					for (GoodsID goodsID : goodsIDList) {
						
						try {
							GoodsDetailList goodsDetailList = new GoodsInfoToEntity().get1GoodsDetailWithSkuidToEntity(goodsID.getItemid());
							if(goodsDetailList!=null&&goodsDetailList.getItemdetail().size()!=0){
								list.addAll(goodsDetailList.getItemdetail());
							}
							
						} catch (Exception e) {log.error("-------------"+e.getMessage()+"-----------",e);}
					}
					System.out.println((new Date( ).getTime()-time1)/1000+"秒+++++++++++++++++++");
					//一次批处理35个插入数据库,因为多了会拼接sql溢出
					/*iCRUDmanDB.insertListBiaoShangPinXiangQing(list);*/
					batchGoodsDetailTemp.save(list);
					System.out.println((new Date( ).getTime()-time1)/1000+"秒====================");
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				log.debug("-----------------商品详情临时库第"+(n+1)+"页建立结束-----------------");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		System.out.println("商品详情临时库建立结束");
		log.debug("-----------------商品详情临时库建立结束-----------------");
	}

	// ////////////////////////4,上下架信息临时库////////////////////////////////////////////////////
//	@Scheduled(cron = "#{configProperties['syncTime']}")
	public void f() {
		System.out.println("执行---------------------!!!");
	}
//商品上下架信息,接口支持批量查询//所以我就一次从接口拿出50个
	public void tongBuShangPinShangXiaJia() {
		System.out.println("商品上下架临时库建立开始");
		BatchGoodsSaleStatus batchGoodsSaleStatus = new BatchGoodsSaleStatus( );
		log.debug("-----------------商品上下架临时库建立开始-----------------");
		try {
			// 先删除商品所有上下架信息
			iCRUDmanDB.deletAllShangXiaJia();
			// 查询所有商品id
			// List<GoodsID> goodsIDList = iCRUDmanDB.queryAllGoodsID();
			// 建议批处理,一次拿出50个,因为一次拼接50个字符串到中粮查询50个比较好
			// 拿到所有itemid的总数
			Integer allGoosIDCount = iCRUDmanDB.queryAllGoosIDCount();
			FenYe fy=new FenYe( );
			fy.setZongJiLuShu(allGoosIDCount);
			fy.setMeiYeXianShiShu(50);
			fy.setZongYeShu();
			// 循环每一页,对每一页的goodsid进行操作,拿到该id的上下架信息后放入上下架临时库
			for (int n = 0; n < fy.getZongYeShu(); ++n) {
				log.debug("-----------------商品上下架临时库第"+(n+1)+"页建立开始-----------------");
				try {
					// 拿到当前页
					fy.setDangQianYe( n + 1);
					// 拿到当前页的所有itemid(其实就是goodsid)实体
					List<GoodsID> query50GoodsIDList = iCRUDmanDB.query50GoodsID(fy.getDangQianYe());
					List<String> idList = new ArrayList<String>();
					String[] a = new String[query50GoodsIDList.size()];
					for (GoodsID goodsID : query50GoodsIDList) {
						idList.add(goodsID.getItemid().trim());
					}
					String[] strArr = idList.toArray(a);
					// 得到商品上下架的一页信息(每条信息包括itemid和上下架的状态)
					GoodsUp1AndDown0StatusList itemstatus = new GoodsInfoToEntity().goodsUp1AndDown0StatusListToEntity(strArr);
//					iCRUDmanDB.insertShangPinShangXiaJia(itemstatus.getItemstatus());
					batchGoodsSaleStatus.save(itemstatus.getItemstatus());
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
				log.debug("-----------------商品上下架临时库第"+(n+1)+"页建立结束-----------------");
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug("-----------------商品上下架临时库建立结束-----------------");
	}

	// ///////////////////5,商品图片信息临时库//该中粮接口支持批处理/////////////////////////////////////////////////////////
	public void tongBuShangPinTuPianXinXi() {
		System.out.println("商品图片信息临时库建立开始");
		BatchImageDetail batchImageDetail = new BatchImageDetail( );
		log.debug("-----------------商品图片信息临时库建立开始-----------------");
		try {
			// 首先删除商品图片信息
			iCRUDmanDB.deleteAllTuPian();
			// List<GoodsID> goodsIDList = iCRUDmanDB.queryAllGoodsID();
			// 使用分页,一页放如1000条进行循环
			// 初始化分页工具类
			FenYe fy=new FenYe( );
			// 统计数据库有几条GoodsID的记录//suo_you_shang_pin_id_lin_shi_ku
			// 拿到所有itemid的总数
		
			fy.setZongJiLuShu(iCRUDmanDB.queryAllGoosIDCount());
			
			// 设置每页显示数//设置完毕,等会通过传入的当前页自动进行计算
			fy.setMeiYeXianShiShu(50);
			// 得到总页数
			fy.setZongYeShu();
			// 循环每一页
			for (int n = 0; n < fy.getZongYeShu(); ++n) {
				try {
					log.debug("-----------------商品图片信息第"+(n+1)+"页临时库建立开始-----------------");
					fy.setDangQianYe(n+1);;
					List<GoodsID> someGoodsIDList = iCRUDmanDB.querySomeGoodsID(fy.getDangQianYe(),fy.getMeiYeXianShiShu());

					List<String> idList = new ArrayList<String>();
					String[] a = new String[someGoodsIDList.size()];
					for (GoodsID goodsID : someGoodsIDList) {
							idList.add(goodsID.getItemid().trim());
					}
					//将收集的list变为数组
					a=idList.toArray(a);
//					System.out.println(idList);
					try {
						ImageDetailObjList imageDetailObjList = new GoodsInfoToEntity().getGoodsImagesToEntity(a,"itemimage");
//						if(true) {
						if (imageDetailObjList != null) {
							List<ImageDetailObj> itemimage = imageDetailObjList.getItemimage();
							//log.debug("-------------"+itemimage.size()+"---------------------");
							List<shang_pin_tu_pian> list = new ArrayList<shang_pin_tu_pian>();
							for (ImageDetailObj imageDetailObj : itemimage) {
								
								try {
									shang_pin_tu_pian shang_pin_tu_pian = new shang_pin_tu_pian();
									shang_pin_tu_pian.setGoodsid(imageDetailObj.getGoodsid());
									List<ImageObj> image = imageDetailObj.getImage();
									ImageObj imageObj1=null;
									ImageObj imageObj2 = null;
									ImageObj imageObj3 = null;
									ImageObj imageObj4 = null;
									try {
										imageObj1 = image.get(0);
									} catch (Exception e2) {}
									try {
										imageObj2 = image.get(1);
									} catch (Exception e2) {}
									try {
										imageObj3 = image.get(2);
									} catch (Exception e2) {}
									try {
										imageObj4 = image.get(3);
									} catch (Exception e2) {}
									try {
										
										if(imageObj1!=null&&imageObj1.getPath()!=null&&!"".equals(imageObj1.getPath())){
											shang_pin_tu_pian.setPath1(imageObj1.getPath());
										}else{
											if(imageObj2!=null&&imageObj2.getPath()!=null&&!"".equals(imageObj2.getPath())){
												shang_pin_tu_pian.setPath1(imageObj2.getPath());
											}else{
												if(imageObj3!=null&&imageObj3.getPath()!=null&&!"".equals(imageObj3.getPath())){
													shang_pin_tu_pian.setPath1(imageObj3.getPath());
												}else{
														shang_pin_tu_pian.setPath1(imageObj4.getPath());
												}
											}
										}
										shang_pin_tu_pian.setIsprimary1(imageObj1.getIsprimary());
									} catch (Exception e1) {
									}
									try {
										
										if(imageObj2!=null&&imageObj2.getPath()!=null&&!"".equals(imageObj2.getPath())){
											shang_pin_tu_pian.setPath2(imageObj2.getPath());
										}else{
											if(imageObj1!=null&&imageObj1.getPath()!=null&&!"".equals(imageObj1.getPath())){
												shang_pin_tu_pian.setPath2(imageObj1.getPath());
											}else{
												if(imageObj3!=null&&imageObj3.getPath()!=null&&!"".equals(imageObj3.getPath())){
													shang_pin_tu_pian.setPath2(imageObj3.getPath());
												}else{
													shang_pin_tu_pian.setPath2(imageObj4.getPath());
												}
											}
										}
										shang_pin_tu_pian.setIsprimary2(imageObj2.getIsprimary());
									} catch (Exception e) {
									}
									try {
										
										if(imageObj3!=null&&imageObj3.getPath()!=null&&!"".equals(imageObj3.getPath())){
											shang_pin_tu_pian.setPath3(imageObj3.getPath());
										}else{
											if(imageObj2!=null&&imageObj2!=null&&!"".equals(imageObj2.getPath())){
												shang_pin_tu_pian.setPath3(imageObj2.getPath());
											}else{
												if(imageObj1!=null&&imageObj1.getPath()!=null&&!"".equals(imageObj1.getPath())){
													shang_pin_tu_pian.setPath3(imageObj1.getPath());
												}else{
													shang_pin_tu_pian.setPath3(imageObj4.getPath());
												}
											}
										}
										shang_pin_tu_pian.setIsprimary3(imageObj3.getIsprimary());
									} catch (Exception e) {
									}
									try {
										
										if(imageObj4!=null&&imageObj4.getPath()!=null&&!"".equals(imageObj4.getPath())){
											shang_pin_tu_pian.setPath4(imageObj4.getPath());
										}else{
											if(imageObj3!=null&&imageObj3.getPath()!=null&&!"".equals(imageObj3.getPath())){
												shang_pin_tu_pian.setPath4(imageObj3.getPath());
											}else{
												if(imageObj2!=null&&imageObj2.getPath()!=null&&!"".equals(imageObj2.getPath())){
													shang_pin_tu_pian.setPath4(imageObj2.getPath());
												}else{
													shang_pin_tu_pian.setPath4(imageObj1.getPath());
												}
											}
										}
										shang_pin_tu_pian.setIsprimary4(imageObj4.getIsprimary());
									} catch (Exception e) {
									}
									list.add(shang_pin_tu_pian);
							
								} catch (Exception e) {
									// TODO 自动生成的 catch 块
									e.printStackTrace();
								}
							}
//							iCRUDmanDB.insertShangPinTuPianXinXi(list);
							
							batchImageDetail.save(list);;
							log.debug("-----------------商品图片信息第"+(n+1)+"页临时库建立结束-----------------");
						}
					} catch (Exception e) {
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug("-----------------商品图片信息临时库建立结束-----------------");
		System.out.println("商品图片信息临时库建立结束");
	}

	// ///////////////////////////6,商品好平度临时入库/////其实海商好评度表里面就没有放入////////////////////////////////////////////
	/*public void tongBuShangPinHaoPingDu() {
		System.out.println("商品好评度临时库建立开始");
		try {
			// 删除所有商品的好评度
			iCRUDmanDB.deleteShangPinHaoPingDu();
			// 得到所有id的数组
			// List<GoodsID> goodsIDList = iCRUDmanDB.queryAllGoodsID();
			// 使用分页
			// 初始化分页工具类
			PageBeanGeneralOfSqlServer<GoodsID> pageBeanGeneralOfSqlServer = new PageBeanGeneralOfSqlServer<GoodsID>();
			// 统计数据库有几条GoodsID的记录//suo_you_shang_pin_id_lin_shi_ku
			// 拿到所有itemid的总数
			Integer allGoosIDCount = iCRUDmanDB.queryAllGoosIDCount();
			int allGoodsIDCoun = 0;
			if (allGoosIDCount != null) {
				allGoodsIDCoun = allGoosIDCount;
			}
			// 给分页工具设置总记录数
			pageBeanGeneralOfSqlServer.setTotalRecord(allGoodsIDCoun);
			// 设置每页显示数//设置完毕,等会通过传入的当前页自动进行计算
			pageBeanGeneralOfSqlServer.setPerPageRecord(1000);
			// 得到总页数
			Integer totalPageCount = pageBeanGeneralOfSqlServer.getTotalPage();
			// 循环每一页
			for (int n = 0; n < totalPageCount; ++n) {
				try {
					int currPage = n + 1;
					List<GoodsID> someGoodsIDList = iCRUDmanDB
							.querySomeGoodsID(currPage, 1000);
					for (GoodsID goodsID : someGoodsIDList) {
						try {
							String id = goodsID.getItemid().trim( );
							CommentsObjList CommentsObjList = new GoodsInfoToEntity()
									.getGoodsCommentsToEntity(new String[] { id });
							// System.out.println(CommentsObjList);
							List<CommentsObj> itemevaluate = CommentsObjList
									.getItemevaluate();
							iCRUDmanDB.insertShangPinHaoPingDu(itemevaluate);
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		System.out.println("商品好评度临时库建立结束");
	}*/

	////////////////////////////////////7,商品价格临时入库//////////////////////////////////////////
	public void tongBuShangPinJiaGe() {
		BatchTempPrice batchTempPrice = new BatchTempPrice( );
		System.out.println("商品价格临时库建立开始");
		log.debug("-----------------商品价格临时库建立开始-----------------");
		try {
			// 删除所有商品价格
			iCRUDmanDB.deleteShangPinJiaGe();
			// 得到所有id的数组
			// List<GoodsID> goodsIDList = iCRUDmanDB.queryAllGoodsID();
			// 分页拿到GoodsID
			// 初始化分页工具类
			FenYe fy=new FenYe( );
			// 统计数据库有几条GoodsID的记录//suo_you_shang_pin_id_lin_shi_ku
			// 拿到所有itemid的总数
			//设置总记录数
			fy.setZongJiLuShu(iCRUDmanDB.queryAllGoosIDCount());
			// 设置每页显示数//设置完毕,等会通过传入的当前页自动进行计算
			fy.setMeiYeXianShiShu(50);
			fy.setZongYeShu();
			// 循环每一页
			for (int n = 0; n < fy.getZongYeShu(); ++n) {
				log.debug("-----------------商品价格临时库建立开始第"+(n+1)+"页-----------------");
				try {
					fy.setDangQianYe(n+1);;
					List<GoodsID> someGoodsIDList = iCRUDmanDB.querySomeGoodsID(fy.getDangQianYe(),fy.getMeiYeXianShiShu());
					if(someGoodsIDList!=null&&someGoodsIDList.size()!=0){
						List<String> list00=new ArrayList<String>( );
						String[ ]aaa=new String[someGoodsIDList.size()];
						for (GoodsID goodsID : someGoodsIDList) {
								list00.add(goodsID.getItemid().trim());
						}
						aaa=list00.toArray(aaa);
						GoodsPriceList GoodsPriceList = new GoodsInfoToEntity().getGoodsPricesWithSkuidsToEntity(aaa);
						if(GoodsPriceList!=null&& GoodsPriceList.getPrice().size()!=0){
							List<GoodsPriceDetail> prices = GoodsPriceList.getPrice();
							// System.out.println(price);
//							iCRUDmanDB.insertShangPinJiaGe(prices);
							batchTempPrice.save(prices);
						}	
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		System.out.println("商品价格临时库建立结束");
		log.debug("-----------------商品价格临时库建立结束-----------------");
	}
	
	// /////////////////////////////同步库存///////////////////////////////////////////////

	// /////////////////////海商product商品表同步///////////////////////////////////////////////////////
	public void ProductRuKuHaiShng() {
		System.out.println("海商商品表同步开始！");
		ProductsCategoryAssociate productsCategoryAssociate = new ProductsCategoryAssociate( );
		log.debug("-----------------海商商品表同步开始！-----------------");
		try {
			// 先删除整个商品库的商品//不能这样搞,要不然原来的信息就没了//可以判断原来的商品是否存在,通过名字判断
			// System.out.println(iCRUDmanDB.deleteHaiShangProduct());

			// 得到所有商品id
			// List<GoodsID> GoodsIDList = iCRUDmanDB.queryAllGoodsID();
			// System.out.println(GoodsIDList.size());
			// 分页拿到id
			// 初始化分页工具类
			FenYe fy=new FenYe( );
			// 统计数据库有几条GoodsID的记录//suo_you_shang_pin_id_lin_shi_ku
			// 拿到所有itemid的总数
			// 给分页工具设置总记录数
			fy.setZongJiLuShu(iCRUDmanDB.queryAllGoosIDCount());;
			// 设置每页显示数//设置完毕,等会通过传入的当前页自动进行计算
			fy.setMeiYeXianShiShu(42);
			// 得到总页数
			fy.setZongYeShu();
			// 循环每一页
			for (int n = 0; n < fy.getZongYeShu(); ++n) {
				log.debug("-----------------海商商品表第"+(n+1)+"页同步开始！-----------------");
				try {
					fy.setDangQianYe(n + 1);
					List<GoodsID> someGoodsIDList = iCRUDmanDB.querySomeGoodsID(fy.getDangQianYe(),fy.getMeiYeXianShiShu());
					for (GoodsID GoodsID : someGoodsIDList) {
							Hishop_Products Hishop_Products = new Hishop_Products();
							GoodsDetail goodsDetail = null;
							shang_pin_tu_pian shang_pin_tu_pian = null;
							GoodsPriceDetail GoodsPriceDetail = null;
							
								try {
										goodsDetail = iCRUDmanDB.getDetailById(GoodsID);
//										if(true){
										if(goodsDetail!=null&&goodsDetail.getGoodsid()!=null&&!"".equals(goodsDetail.getGoodsid())){
												// 得到图片详情
												try {shang_pin_tu_pian = iCRUDmanDB.getImage(GoodsID);} catch (Exception e) {}
												//如果图片为空,就不再插入该条数据
//												if (true) {
												if (shang_pin_tu_pian.getPath1()!=null&&!"".equals(shang_pin_tu_pian.getPath1())) {
													// 根据id得到价格
													try {
														GoodsPriceDetail = iCRUDmanDB
																.getPrice(GoodsID);
													} catch (Exception e) {
													}
													// System.out.println(category);
													try {
														Hishop_Products
																.setCategoryId(Integer
																		.parseInt(goodsDetail
																				.getCategory()));
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setProductName(goodsDetail
																		.getGoodsname());
													} catch (Exception e) {
													}// 封装商品名字
													// 条形码不能封装在ProductCode中了,因为ProductCode将来由商家自己编辑
													// try
													// {Hishop_Products.setProductCode(goodsDetail.getGoodsbarcode());}
													// catch (Exception e)
													// {e.printStackTrace();}//封装商品货号//条形码
													//2016/12/13加,为了让商品在中粮的唯一标识进入海商,就把中粮的商品id放入海商的商品货号字段中
													try {
														Hishop_Products
																.setProductCode(goodsDetail
																		.getGoodsid());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setUnit(goodsDetail
																		.getUnit());
													} catch (Exception e) {
													}// 封装商品单位
													try {
														Hishop_Products
																.setDescription(goodsDetail
																		.getProdescription());
													} catch (Exception e) {
													}// 封装商品描述
													try {//如果价格是0或者空,就设置商品下架
														String price = GoodsPriceDetail
																.getPrice();
														if ("0".equals(price)
																|| price == null
																|| "".equals(price)) {
															Hishop_Products
																	.setSaleStatus(0);
														} else {
															Hishop_Products
																	.setSaleStatus(Integer
																			.parseInt(goodsDetail
																					.getPubflag()));
														}
													} catch (Exception e) {
													}// 商品上下架
													try {
														Hishop_Products
																.setImageUrl1(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}// 图片路径
													try {
														Hishop_Products
																.setImageUrl2(shang_pin_tu_pian
																		.getPath2());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setImageUrl3(shang_pin_tu_pian
																		.getPath3());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setImageUrl4(shang_pin_tu_pian
																		.getPath4());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setImageUrl5(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													//2016/12/13增加
													try {
														Hishop_Products
																.setThumbnailUrl40(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl60(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl100(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl160(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl180(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl220(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl310(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													try {
														Hishop_Products
																.setThumbnailUrl410(shang_pin_tu_pian
																		.getPath1());
													} catch (Exception e) {
													}
													//2016/12/13增加
													try {
														/*ImageUrl1,
														ImageUrl2,ImageUrl3,ImageUrl4,ImageUrl5,ThumbnailUrl40,ThumbnailUrl60,ThumbnailUrl100,
														ThumbnailUrl160,ThumbnailUrl180,ThumbnailUrl220,ThumbnailUrl310,ThumbnailUrl410*/
														//2016/12/13增加
														try {
															Hishop_Products
																	.setMarketPrice(new BigDecimal(
																			GoodsPriceDetail
																					.getPrice()));
														} catch (Exception e) {
														}
														// String price
														// ="";//这个应该是成本价,不能被封装进市场价,否则会把成本价当成市场价显示在页面
														// try {price = GoodsPriceDetail.getPrice();}
														// catch (Exception e) {e.printStackTrace();}
														// System.out.println(price);
														// //封装商品价格//这个是成本价,不能封装在市场价格里,所以在商品规格详细表中再封装这个价格(市场价格)
														// Hishop_Products.setMarketPrice(new
														// BigDecimal(price));
														try {//分类
															Hishop_Products
																	.setMainCategoryPath(productsCategoryAssociate
																			.getMainCategoryPath(goodsDetail
																					.getCategory()));
														} catch (Exception e) {
														}
														//下面我要把供应商的SupplierId插入
														Hishop_Products.setSupplierId(SpringUtil.getBean(GetSupplierId.class).getSupplierId());//假设1是海商吧
														//											Hishop_Products.getProductCode()!=null&&
														//下面更改为先删后插入
														if (iCRUDmanDB
																.ifExsitProducts(Hishop_Products) <= 0) {//20170324我用ProductCode来做标准// 此时数据库没有一样的记录,要插入//主要根据商品名字ProductName判断//我是以中粮的那个注册编码当的货号
															// select count(ProductName) from
															// Hishop_Products where
															// ProductCode=#{ProductName};
															// 由于原来郑总说了这个ProductCode是将来在后台商家自己编辑的,所以ProdectCode不再作为标识是否已经在表中存在该商品,只能用ProductName做标识
															// //插入该条商品到数据库
															iCRUDmanDB
																	.insertToHaiShangProduct(Hishop_Products);
														} else {// shang mian shi bu cun zai gai tiao ji
																// lu de shi hou,jiu cha ru yi tiao ,xia
																// mian shi cun zai le jiu geng xin gai
																// ProductName xia de na ge shangXiaJia
																// xin xi
															//由于海商商品表统计了卖出多少的信息,所以不能随便删除一条记录,只能更新一条记录
															log.debug("------------------该商品已经存在,开始更新该商品----------------");
															//									iCRUDmanDB.updateSaleStauts(Hishop_Products);//这个只更新上下架,太少了
															//要更新Description saleStatus  和图片路径
															iCRUDmanDB
																	.updateHishop_Products(Hishop_Products);
														}

														/*//2016 12 13 将上面的检查是否存在再插入的方式更换为 先删除再插入//注意这个是根据name删除的,如果有必要,可以把xml中的sql随时改为根据productCode来删除
														System.out.println("删除"+iCRUDmanDB.deleteOneOfHishop_Products(Hishop_Products)+"条");
														// //插入该条商品到数据库
														System.out.println("插入"+iCRUDmanDB.insertToHaiShangProduct(Hishop_Products)+"条");*/

													} catch (Exception e) {
														log.error(
																e.getMessage(),
																e);
													}
												}
										}
								} catch (Exception e) {}
								
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug("-----------------海商商品表同步结束！-----------------");
		System.out.println("海商商品表同步结束！");
	}

	// //////该程序依赖商品详情表和海商商品表和价格临时表,也就是说必须在上面3表同步完成后才能启动该程序同步规格表//////////////////////把商品库的productid等信息放入sku规格库中////////////////////////////////////////////////
	// 同步海商自带商品规格表//该表跟商品表通过productID联系
	public void tongBuShangPinGuiGeBiao() {// tongbu shang pin gui ge biao
		System.out.println("海商商品规格表同步开始！");log.debug("-----------------海商商品规格表同步开始！！-----------------");
		try {
			// List<Hishop_SKUs>hishop_SKUsList=new ArrayList<Hishop_SKUs>( );
			// 得到海商的所有商品详情,从自建表得到
			// List<GoodsDetail> goodsDetailList =
			// iCRUDmanDB.getAllLinShiKuGoodsDetail( );
			// 初始化分页工具类
			FenYe fy=new FenYe( );
			// 分页拿到海商商品详情//临时表
			// 统计总页数
			// 给分页工具设置总记录数
			fy.setZongJiLuShu(iCRUDmanDB.getCountOfAllGoodsDetail());
			// 设置每页显示数//设置完毕,等会通过传入的当前页自动进行计算
			fy.setMeiYeXianShiShu(35);;
			// 设置总页数
			fy.setZongYeShu();;
			// 循环每一页
			for (int n = 0; n < fy.getZongYeShu(); ++n) {
				log.debug("-----------------海商商品规格表第"+(n+1)+"页同步开始！！-----------------");
				try {
					fy.setDangQianYe(n+1);
					List<GoodsDetail> goodsDetailList = iCRUDmanDB.getSomeGoodsDetail(fy.getDangQianYe(), fy.getMeiYeXianShiShu());// 每次查询1000条
					for (GoodsDetail goodsDetail : goodsDetailList) {// 商品量不大,一次拿入内存
						try {
							// 从海商商品表中找到该商品的productId(数据库自动生成)//用goodsName找
							String goodsId = goodsDetail.getGoodsid();
							
							// 用中粮的商品id找到海商的商品id
							String productId = iCRUDmanDB.getProductIdInHiShopUseGoodsId(goodsId);
							if (productId != null) {
								// 得到商品规格表实体
								Hishop_SKUs hishop_SKUs = new Hishop_SKUs();
								// 设置商品规格的重量
								hishop_SKUs.setWeight(new BigDecimal(goodsDetail.getWeight()));
								// 得到商品的goodsid
								String goodsid = goodsDetail.getGoodsid();
								// 到该goodsid对应的商品价格//查询商品价格临时表
								GoodsID goodsID = new GoodsID();
								goodsID.setItemid(goodsid);
								// 避免有重复的goodsID,使用集合
								List<GoodsPriceDetail> priceS = iCRUDmanDB.getPriceS(goodsID);
								// 设置商品规格的价格(本价)
								String price = "0";Double pricee=0D;BigDecimal price1=null;
								try {
									// 选重复的id的第一个//按找实际情况,中粮的id是不应该有重复的
									GoodsPriceDetail goodPriceDetail = priceS.get(0);
									price = goodPriceDetail.getPrice();
//									pricee=Double.parseDouble(price);
									
								} catch (Exception e) {
								}
//								if(Double.parseDouble(price)>=1){
								try {
									if(price!=null&&!"".equals(price)&&!"0".equals(price)){
										hishop_SKUs.setCostPrice(new BigDecimal(0));
										hishop_SKUs.setSalePrice(new BigDecimal(price));
									}else{
										//此时要把该条商品下架,因为一口价 SalePrice拿过来是0被我自己加成1了//改成0,不再加1,到时候能知道多少有问题
										iCRUDmanDB.setSaleStatus0(goodsId);//下架该商品,因为该商品有问题
										hishop_SKUs.setCostPrice(new BigDecimal(0));
										hishop_SKUs.setSalePrice(new BigDecimal(5201314));
									}
								} catch (Exception e) {iCRUDmanDB.setSaleStatus0(goodsId);}
//								hishop_SKUs.setCostPrice(new BigDecimal(price));
								// 自动设置SalePrice, 在Hishop_SKUs中实现计算
								// SalePrice=CostPrice+10元
//								hishop_SKUs.setSalePrice(1D);
								// 设置规格表的skus的productid
								hishop_SKUs.setProductId(Integer.parseInt(productId));
								// //////////一条一条的插入数据库,因为要过滤该条记录是否在数据库存在,所以不能用批处理////////////
								// 设置skuid
								hishop_SKUs.setSkuId(productId + "_0");
								
								
								
								// 当数据库没有该条记录的时候插入该条记录//原来通过skuid比较是否存在,
								int hishop_SKUsExsit = iCRUDmanDB.isHishop_SKUsExsit(hishop_SKUs);
								if (hishop_SKUsExsit == 0) {// 先判断是否存在,不存在,再插入
									iCRUDmanDB.insertHishop_SKUs2(hishop_SKUs);
								}else{
									//存在就更新价格
									iCRUDmanDB.updateHishop_SKUs(hishop_SKUs);
								}
								
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		log.debug("-----------------海商商品规格表同步结束！！-----------------");
		System.out.println("海商商品规格表同步结束！");
	}

	// ///同步库存到临时库/////同步过来是隔夜的,后来决定让前台直接调库存接口,这里不再同步,要同步的话下面还要改动////////////////////////////////////////////////////////////////////
	// //////通过海商商品库的所有商品得到ProductName,在通过这个ProductName找到商品信息临时表中的goodid,然后用goodsid和门店区域代码进行查询库存,之后放入商品规格表中的库存///
	// ///////////////////////同步中粮库存,此表必须在门店建立之后产生门店表,商品表,规格详细表,然后查询库存到门店表//////////////////////////////////////////
	// 思路,在门店库存表Hishop_StoreStock中根据3个大区仓库,循环中粮大概10000条数据
	// (1)首先从门店表Hishop_Stores中拿到所有记录进行循环,目的是对三个仓库输入10000条商品数据的信息
	/*public void syncZLStoreUseWarehouse() {// 用仓库同步中粮商品库存
		try {
			System.out.println("同步商品在各个大区的库存开始！");
			// 得到所有的门店中所有的数据实体
			List<Hishop_Stores> Hishop_StoresList = iCRUDmanDB
					.getAllHishop_Stores();
			// 循环所有门店,进行门店库存表的数据插入
			for (Hishop_Stores hishop_Store : Hishop_StoresList) {
				try {
					// 得到门店的id
					int storeId = hishop_Store.getStoreId();
					// 得到门店对应的大区名字
					String storeName = hishop_Store.getStoreName();
					// 截取大区名字(
					// )内的大区代码,就获得当前大区的代码(只有0,100,200三个)//该代码就是大区仓库代码,可以用来组合JSON查询库存量
					// 大区仓库1(0)就是给门店起的名字
					String WarehouseId = storeName.substring(
							storeName.indexOf("(") + 1, storeName.length() - 1);
					// 得到所有商品记录,其中只有productId和ProductName能用,其他属性不能用(实际上线大概有10000条)
					// List<Hishop_Products> hishop_Productlist =
					// iCRUDmanDB.getAllProductIdAndProductName();
					// System.out.println(iCRUDmanDB.getSomeProductIdAndProductName(1,1000));
					// 初始化分页工具类
					PageBeanGeneralOfSqlServer<Hishop_Products> pageBeanGeneralOfSqlServer = new PageBeanGeneralOfSqlServer<Hishop_Products>();
					// 分页拿到海商商品详情//临时表
					// 统计总商品数量
					Integer countOfAllHishop_Products = iCRUDmanDB
							.getAllCountOfHishop_Products();
					int count = 0;
					if (countOfAllHishop_Products != null) {
						count = countOfAllHishop_Products;
					}
					// 给分页工具设置总记录数
					pageBeanGeneralOfSqlServer.setTotalRecord(count);
					// 设置每页显示数//设置完毕,等会通过传入的当前页自动进行计算
					pageBeanGeneralOfSqlServer.setPerPageRecord(1000);
					// 得到总页数
					Integer totalPageCount = pageBeanGeneralOfSqlServer
							.getTotalPage();
					// 循环每一页
					for (int n = 0; n < totalPageCount; ++n) {
						try {
							int currPage = n + 1;
							List<Hishop_Products> hishop_Productlist = iCRUDmanDB
									.getSomeProductIdAndProductName(currPage,
											1000);
							for (Hishop_Products hishop_Products : hishop_Productlist) {
								try {
									// 得到productId//注意productId是主键
									Integer productId = hishop_Products
											.getProductId();
									if (productId != 0 && productId != null) {
										// 通过productId在商品规格详细表中找到skuid
										Hishop_SKUs Hishop_SKUs1 = iCRUDmanDB
												.getSkuidByProductIdInHishop_SKUs(productId);
										if(Hishop_SKUs1!=null){
											String SkuId = Hishop_SKUs1.getSkuId();
											if (SkuId != null && !"".equals(SkuId)) {
												// 得到商品名字//ProductName
												String productName = hishop_Products
														.getProductName();
												// 通过productName得到商品详情临时表的goodsId(就是中粮商品id)
												String goodsid = iCRUDmanDB
														.getGoodsIdByGoodsNameInGoodsDetailLinShiTab(productName);
												// 查询stock(库存),通过大区代码和商品在中粮的goodsid来查找
												String[] skuids = new String[] { goodsid };

												// 门店库存数据库添加数据
												Hishop_StoreStock hishop_StoreStock = new Hishop_StoreStock();
												hishop_StoreStock
														.setStoreId(storeId);
												hishop_StoreStock
														.setProductID(productId);
												hishop_StoreStock.setSkuId(SkuId);
												// 得到商品库存//mu di shi ru guo cha bu
												// dao gai tiao ji lu de ku cun jiu
												// biao shi mei you ku cun
												KuCunList kuCunList = new GoodsInfoToEntity()
														.getInventoryInfoWithWarehouseIdAndSkuidsToEntity(
																WarehouseId, skuids);
												if (kuCunList.getInventory().size() != 0) {
													String inventory = kuCunList
															.getInventory().get(0)
															.getInventory();
													hishop_StoreStock
															.setStock(Integer
																	.parseInt(inventory));
												}

												// 判断hishop_StoreStock在数据库中是否已经存在(通过productId和StoreId判断)
												int ifExsitHishop_StoreStock = iCRUDmanDB
														.ifExsitHishop_StoreStock(hishop_StoreStock);
												if (ifExsitHishop_StoreStock == 0) {//不存在就插入
													iCRUDmanDB
															.insertHishop_StoreStock(hishop_StoreStock);
												} else if (ifExsitHishop_StoreStock == 1) {// 有一条记录的时候,代表已经存在,只需要update更新
													// 通过productID和StoreId一起来定位
													iCRUDmanDB
															.updateHishop_StoreStock(hishop_StoreStock);
												} else {
													System.out.println("中粮商品有重复？！");
												}
											}
										}
									}
								} catch (Exception e) {
									log.error(e.getMessage(), e);
								}
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		System.out.println("同步商品在各个大区的库存结束！");
	}

	// //////////////////////////商品规格表Hishop_skus中的库存通过productId找到门店表中Hishop_sotreStock某个一商品的所有门店库存进行叠加/////////////////////////////////////////////////
	// 这个表必须在Hishop_sotreStock商品库存表更新后才能更新
	// Hishop_skus商品规格中的库存=count(Hishop_sotreStock门店)库存
	public void syncShangPinGuiGeKuCun_UseMenDianKuCun() {
		System.out.println("同步商品在各个大区的库存之和开始！");
		try {
			// 分页工具初始化
			PageBeanOfHishop_SKUs pageBeanOfHishop_SKUs = new PageBeanOfHishop_SKUs();
			// 分页拿到所有的商品规格表的productID,将来循环拿到该id下所有大区的库存之和,然后放入商品规格表的库存
			// 先查询总记录数
			int allRecordCountOfHishop_SKUs = iCRUDmanDB
					.getAllRecordCountOfHishop_SKUs();
			// 分页工具设置总记录数//每页数默认5000
			// 设置总记录数
			pageBeanOfHishop_SKUs.setTotalRecord(allRecordCountOfHishop_SKUs);
			// 得到总页数//先有总记录数,后有总页数
			int totalPage = pageBeanOfHishop_SKUs.getTotalPage();
			// pageBeanOfHishop_SKUs.setCurrentPage(1);
			// iCRUDmanDB.getCurrentPageRecord(pageBeanOfHishop_SKUs.getPerPageRecord(),pageBeanOfHishop_SKUs.getBeforeCurrentPageTotalRecord());
			// 遍历总页数//对每页进行操作
			for (int n = 0; n < totalPage; ++n) {
				try {
					// 设置当前页
					pageBeanOfHishop_SKUs.setCurrentPage(n + 1);
					// 得到当前页的所有规格表的productID//通过实体查询数据库
					List<Hishop_SKUs> hishop_SKUsList = iCRUDmanDB
							.getCurrentPageRecord(pageBeanOfHishop_SKUs
									.getPerPageRecord(), pageBeanOfHishop_SKUs
									.getBeforeCurrentPageTotalRecord());
					// 循环list,拿到所有productID
					for (Hishop_SKUs hishop_SKUs : hishop_SKUsList) {
						try {
							int productId = hishop_SKUs.getProductId();
							// 从门店库存表中叠加productID相同的库存数量
							Integer allStock = iCRUDmanDB
									.addAllStockWhereHaveSameProductID(productId);
							if (allStock != null) {// 当传入的productID在Hishop_StoreStock中不存在的时候,会返回null
								// 找到Hishop_SKUs商品规格详细表中相同的ProductID的商品,把该库存allStock库存插入进去
								iCRUDmanDB.updateHishop_SKUsStock(productId,
										allStock);
							}
						} catch (Exception e) {
							log.error(e.getMessage(), e);
						}
					}
				} catch (Exception e) {
					log.error(e.getMessage(), e);
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		System.out.println("同步商品在各个大区的库存之和结束！");
	}*/

	// ////////////////////////////////////////////////////////////////////////////
	
	//将分类Exel插入海商的分类表
	public void doCategoryTab( ){
		if(iCRUDmanDB.isCategoryTbHaveData( )<1){//如果数据条数小于1,就从新来一遍
			System.out.println("开始入分类表----------------");
			//先清除表
			iCRUDmanDB.deleteCategoryTb( );
			new CategoryExcelToDb( ).categoryExcelToDb();
		}
	}
	
	
	
	
	
	//关联分类表和海商主商品表,把商品表翻一遍//如果上一个方法能成功,这个方法可以不要
	public void associateCategoryAndProduct( ){
		//其实我在上面的方法中插入的时候已经关联的,这里做个保险
		if(iCRUDmanDB.isProductsCategoryAssociateHaveData( )<1){
			System.out.println("开始关联--------------");
			new ProductsCategoryAssociate( ).f();
		}
			
		
	}
	
	
	
	
	
	// @Scheduled(fixedRate=2000)//第一次任务执行完后每隔2秒执行一次
	@Scheduled(cron = "#{configProperties['syncTime']}")
	// 在第一次任务执行后每天18点50运行一次//其实IOC容器加载的时候该任务就开始执行了
	public void tongBuLinShiBiaoHeHaiShangShangPinKu() {
		log.debug("--------同步商品开始----time=-"+(new Date( ))+"-！！！！！！！！！----------------------------------");
//		System.out.println("海商商品临时库和规格表同步开始！");
		long time1 = new Date().getTime();
		//删除并导入分类表
		doCategoryTab( );
		tongBuShangPinChiYeMaHeMingZi();
		tongBuSuoYouShangPinBianHao();
		tongBuSuoYouShangPinXiangQing();
		// f();
		tongBuShangPinShangXiaJia();
		tongBuShangPinTuPianXinXi();
		//后来蒋总说不用同步好评度了
//		tongBuShangPinHaoPingDu();
		tongBuShangPinJiaGe();
		// 下面两个次序不能变
		// 海商商品库同步
		ProductRuKuHaiShng();
		// 海商规格表同步
		tongBuShangPinGuiGeBiao();
		// 同步各个库区仓库库存
		//syncZLStoreUseWarehouse();
		// 同步商品规格表总库存,其实是一个商品在3个门店中的库存之和
		//syncShangPinGuiGeKuCun_UseMenDianKuCun();
		//关联商品和分类表,这个其实可以不要了,主要是作为保险用,因为前面已经在插入商品的时候做掉了
		associateCategoryAndProduct( );
		//2017_6_19   weekday(1)   12:34:56  add function that: Category tab can select category in background console
		iCRUDmanDB.doSelectCategoryHasChild();
		long time2 = new Date().getTime();
		log.debug("-------------------"+(time2 - time1) / 1000 + "秒----------合计小时数--"+(time2 - time1) / 1000/3600+"小时---------------------");
		log.debug("-------"+(new Date( ))+"----------海商商品临时库和规格表同步结束！---------------------");
	}

	
	// ////////////////////////////////////////////////////////////////////////////
	//
	// ////////////////////////////////////////////////////////////////////////////

	// ////////////////////////////////////////////////////////////////////////////
/*	public static void main(String[] args) {
		String[] configs = {"classpath*:hanhan/zhongLiangTongBuKuKuangJia/xml/b003SpringXml/spring.xml"};
		ApplicationContext ctx = new ClassPathXmlApplicationContext(configs);
		DoYuanFeng doYuanFeng = (DoYuanFeng) ctx.getBean("doYuanFeng");
		 doYuanFeng.f();
//		doYuanFeng.tongBuLinShiBiaoHeHaiShangShangPinKu();
//		doYuanFeng.quYuCangKuDuiZhaoBiao();
		System.out.println("================");
	}*/
	// ////////////////////////////////////////////////////////////////////////////
}
// ////////////////////////////////////////////////////////////////////////////