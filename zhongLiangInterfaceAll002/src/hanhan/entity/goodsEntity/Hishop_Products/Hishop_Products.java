package hanhan.entity.goodsEntity.Hishop_Products;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

public class Hishop_Products {
    int SupplierId = -1;//-1表示没有得到应有的SupplierId
    int CategoryId = 2;//	Int来自商品分类表	分类编号
    int TypeId = 1;//	Int来自商品类型表 NULL	类型编号
    int ProductId;//	Int自动增长	商品编号
    String ProductName = "";//	Nvarchar(200)	商品名称
    String ProductCode = null;//	Nvarchar(50) NULL	商品货号
    String ShortDescription = null;//	Nvarchar(2000) NULL	简单描述
    String Unit = null;//	Nvarchar(50) NULL	计量单位
    String Description = null;//	Ntext NULL	详细描述
    String MobbileDescription = null;//	Ntext NULL	移动端详细描述
    String Title = null;//	Nvarchar(100) NULL	页面标题
    String Meta_Description = null;//	Nvarchar(1000) NULL	页面描述
    String Meta_Keywords = null;//	Nvarchar(1000) NULL	页面关键字
    int SaleStatus = 0;//	int NULL	商品状态（0删除1上架2下架3入库）
    Timestamp AddedDate = new Timestamp(new Date().getTime());//Datetime	添加日期
    Timestamp UpdateDate = new Timestamp(new Date().getTime());//	Datetime default(getdate())	更新日期
    int VistiCounts = 0;//	Int default(0)	访问统计
    int SaleCounts = 0;//	Int default(0)	销售统计
    int ShowSaleCounts = 0;//	Int default(0)	显示的销售统计
    int DisplaySequence = 1;//	Int	显示顺序
    String ImageUrl1 = null;    //Nvarchar(255) NULL	商品原图1路径
    String ImageUrl2 = null;    //Nvarchar(255) NULL	商品原图2路径
    String ImageUrl3 = null;    //Nvarchar(255) NULL	商品原图3路径
    String ImageUrl4 = null;//	Nvarchar(255) NULL	商品原图4路径
    String ImageUrl5 = null;//	Nvarchar(255) NULL	商品原图5路径
    String ThumbnailUrl40 = null;//	Nvarchar(255) NULL	商品40*40的缩略图路径
    String ThumbnailUrl60 = null;//	Nvarchar(255) NULL	商品60*60的缩略图路径
    String ThumbnailUrl100 = null;//	Nvarchar(255) NULL	商品100*100的缩略图路径
    String ThumbnailUrl160 = "";//	nvarchar(255)	商品160*160的缩略图路径
    String ThumbnailUrl180 = null;//	Nvarchar(255) NULL	商品180*180的缩略图路径
    String ThumbnailUrl220 = null;//	Nvarchar(255) NULL	商品220*220的缩略图路径
    String ThumbnailUrl310 = null;//	Nvarchar(255) NULL	商品310*3100的缩略图路径
    String ThumbnailUrl410 = null;//	Nvarchar(255) NULL	商品410*410的缩略图路径
    BigDecimal MarketPrice = new BigDecimal(1);//	Money	市场价
    int BrandId = 1;//	Int来自品牌表	品牌编号
    String MainCategoryPath = "";//	Nvarchar(256)	主分类路径
    String ExtendCategoryPath = null;//	Nvarchar(256) NULL	扩展分类路径1
    String ExtendCategoryPath1 = null;//	Nvarchar(256) NULL	扩展分类路径2
    String ExtendCategoryPath2 = null;    //Nvarchar(256) NULL	扩展分类路径3
    String ExtendCategoryPath3 = null;    //Nvarchar(256) NULL	扩展分类路径4
    String ExtendCategoryPath4 = null;    //Nvarchar(256) NULL	扩展分类路径5
    boolean HasSKU = false;//	Bit	是否开启规格[1已开店，0未开启]
    boolean IsfreeShipping = false;//Bit	是否免费送货
    long TaobaoProductId = 1;//	int NULL	淘宝商品编号
    BigDecimal ReferralDeduct = new BigDecimal(0);//	Money NULL	直接推广提成比例
    BigDecimal SubMemberDeduct = new BigDecimal(0);//	Money NULL	下级会员提成比例
    BigDecimal SubReferralDeduct = new BigDecimal(0);//Money NULL	下级推广员提成比例
    int ShippingTemplateId = 3;//Int	运费模板
    BigDecimal Volume = new BigDecimal(1);//money	;//体积
    BigDecimal SecondLevelDeduct = new BigDecimal(1);//money	二级推广员提成比例
    BigDecimal ThreeLevelDeduct = new BigDecimal(1);//money	三级推广员提成比例

    public Hishop_Products() {
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getShortDescription() {
        return ShortDescription;
    }

    public void setShortDescription(String shortDescription) {
        ShortDescription = shortDescription;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getMobbileDescription() {
        return MobbileDescription;
    }

    public void setMobbileDescription(String mobbileDescription) {
        MobbileDescription = mobbileDescription;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMeta_Description() {
        return Meta_Description;
    }

    public void setMeta_Description(String meta_Description) {
        Meta_Description = meta_Description;
    }

    public String getMeta_Keywords() {
        return Meta_Keywords;
    }

    public void setMeta_Keywords(String meta_Keywords) {
        Meta_Keywords = meta_Keywords;
    }

    public int getSaleStatus() {
        return SaleStatus;
    }

    public void setSaleStatus(int saleStatus) {
        SaleStatus = saleStatus;
    }

    public Timestamp getAddedDate() {
        return AddedDate;
    }

    public void setAddedDate(Timestamp addedDate) {
        AddedDate = addedDate;
    }

    public Timestamp getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        UpdateDate = updateDate;
    }

    public int getVistiCounts() {
        return VistiCounts;
    }

    public void setVistiCounts(int vistiCounts) {
        VistiCounts = vistiCounts;
    }

    public int getSaleCounts() {
        return SaleCounts;
    }

    public void setSaleCounts(int saleCounts) {
        SaleCounts = saleCounts;
    }

    public int getShowSaleCounts() {
        return ShowSaleCounts;
    }

    public void setShowSaleCounts(int showSaleCounts) {
        ShowSaleCounts = showSaleCounts;
    }

    public int getDisplaySequence() {
        return DisplaySequence;
    }

    public void setDisplaySequence(int displaySequence) {
        DisplaySequence = displaySequence;
    }

    public String getImageUrl1() {
        return ImageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        ImageUrl1 = imageUrl1;
    }

    public String getImageUrl2() {
        return ImageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        ImageUrl2 = imageUrl2;
    }

    public String getImageUrl3() {
        return ImageUrl3;
    }

    public void setImageUrl3(String imageUrl3) {
        ImageUrl3 = imageUrl3;
    }

    public String getImageUrl4() {
        return ImageUrl4;
    }

    public void setImageUrl4(String imageUrl4) {
        ImageUrl4 = imageUrl4;
    }

    public String getImageUrl5() {
        return ImageUrl5;
    }

    public void setImageUrl5(String imageUrl5) {
        ImageUrl5 = imageUrl5;
    }

    public String getThumbnailUrl40() {
        return ThumbnailUrl40;
    }

    public void setThumbnailUrl40(String thumbnailUrl40) {
        ThumbnailUrl40 = thumbnailUrl40;
    }

    public String getThumbnailUrl60() {
        return ThumbnailUrl60;
    }

    public void setThumbnailUrl60(String thumbnailUrl60) {
        ThumbnailUrl60 = thumbnailUrl60;
    }

    public String getThumbnailUrl100() {
        return ThumbnailUrl100;
    }

    public void setThumbnailUrl100(String thumbnailUrl100) {
        ThumbnailUrl100 = thumbnailUrl100;
    }

    public String getThumbnailUrl160() {
        return ThumbnailUrl160;
    }

    public void setThumbnailUrl160(String thumbnailUrl160) {
        ThumbnailUrl160 = thumbnailUrl160;
    }

    public String getThumbnailUrl180() {
        return ThumbnailUrl180;
    }

    public void setThumbnailUrl180(String thumbnailUrl180) {
        ThumbnailUrl180 = thumbnailUrl180;
    }

    public String getThumbnailUrl220() {
        return ThumbnailUrl220;
    }

    public void setThumbnailUrl220(String thumbnailUrl220) {
        ThumbnailUrl220 = thumbnailUrl220;
    }

    public String getThumbnailUrl310() {
        return ThumbnailUrl310;
    }

    public void setThumbnailUrl310(String thumbnailUrl310) {
        ThumbnailUrl310 = thumbnailUrl310;
    }

    public String getThumbnailUrl410() {
        return ThumbnailUrl410;
    }

    public void setThumbnailUrl410(String thumbnailUrl410) {
        ThumbnailUrl410 = thumbnailUrl410;
    }

    public BigDecimal getMarketPrice() {
        return MarketPrice;
    }

    public void setMarketPrice(BigDecimal marketPrice) {
        MarketPrice = marketPrice;
    }

    public int getBrandId() {
        return BrandId;
    }

    public void setBrandId(int brandId) {
        BrandId = brandId;
    }

    public String getMainCategoryPath() {
        return MainCategoryPath;
    }

    public void setMainCategoryPath(String mainCategoryPath) {
        MainCategoryPath = mainCategoryPath;
    }

    public String getExtendCategoryPath() {
        return ExtendCategoryPath;
    }

    public void setExtendCategoryPath(String extendCategoryPath) {
        ExtendCategoryPath = extendCategoryPath;
    }

    public String getExtendCategoryPath1() {
        return ExtendCategoryPath1;
    }

    public void setExtendCategoryPath1(String extendCategoryPath1) {
        ExtendCategoryPath1 = extendCategoryPath1;
    }

    public String getExtendCategoryPath2() {
        return ExtendCategoryPath2;
    }

    public void setExtendCategoryPath2(String extendCategoryPath2) {
        ExtendCategoryPath2 = extendCategoryPath2;
    }

    public String getExtendCategoryPath3() {
        return ExtendCategoryPath3;
    }

    public void setExtendCategoryPath3(String extendCategoryPath3) {
        ExtendCategoryPath3 = extendCategoryPath3;
    }

    public String getExtendCategoryPath4() {
        return ExtendCategoryPath4;
    }

    public void setExtendCategoryPath4(String extendCategoryPath4) {
        ExtendCategoryPath4 = extendCategoryPath4;
    }

    public boolean isHasSKU() {
        return HasSKU;
    }

    public void setHasSKU(boolean hasSKU) {
        HasSKU = hasSKU;
    }

    public boolean isIsfreeShipping() {
        return IsfreeShipping;
    }

    public void setIsfreeShipping(boolean isfreeShipping) {
        IsfreeShipping = isfreeShipping;
    }

    public long getTaobaoProductId() {
        return TaobaoProductId;
    }

    public void setTaobaoProductId(long taobaoProductId) {
        TaobaoProductId = taobaoProductId;
    }

    public BigDecimal getReferralDeduct() {
        return ReferralDeduct;
    }

    public void setReferralDeduct(BigDecimal referralDeduct) {
        ReferralDeduct = referralDeduct;
    }

    public BigDecimal getSubMemberDeduct() {
        return SubMemberDeduct;
    }

    public void setSubMemberDeduct(BigDecimal subMemberDeduct) {
        SubMemberDeduct = subMemberDeduct;
    }

    public BigDecimal getSubReferralDeduct() {
        return SubReferralDeduct;
    }

    public void setSubReferralDeduct(BigDecimal subReferralDeduct) {
        SubReferralDeduct = subReferralDeduct;
    }

    public int getShippingTemplateId() {
        return ShippingTemplateId;
    }

    public void setShippingTemplateId(int shippingTemplateId) {
        ShippingTemplateId = shippingTemplateId;
    }

    public BigDecimal getVolume() {
        return Volume;
    }

    public void setVolume(BigDecimal volume) {
        Volume = volume;
    }

    public BigDecimal getSecondLevelDeduct() {
        return SecondLevelDeduct;
    }

    public void setSecondLevelDeduct(BigDecimal secondLevelDeduct) {
        SecondLevelDeduct = secondLevelDeduct;
    }

    public BigDecimal getThreeLevelDeduct() {
        return ThreeLevelDeduct;
    }

    public void setThreeLevelDeduct(BigDecimal threeLevelDeduct) {
        ThreeLevelDeduct = threeLevelDeduct;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Hishop_Products{");
        sb.append("SupplierId=").append(SupplierId);
        sb.append(", CategoryId=").append(CategoryId);
        sb.append(", TypeId=").append(TypeId);
        sb.append(", ProductId=").append(ProductId);
        sb.append(", ProductName='").append(ProductName).append('\'');
        sb.append(", ProductCode='").append(ProductCode).append('\'');
        sb.append(", ShortDescription='").append(ShortDescription).append('\'');
        sb.append(", Unit='").append(Unit).append('\'');
        sb.append(", Description='").append(Description).append('\'');
        sb.append(", MobbileDescription='").append(MobbileDescription).append('\'');
        sb.append(", Title='").append(Title).append('\'');
        sb.append(", Meta_Description='").append(Meta_Description).append('\'');
        sb.append(", Meta_Keywords='").append(Meta_Keywords).append('\'');
        sb.append(", SaleStatus=").append(SaleStatus);
        sb.append(", AddedDate=").append(AddedDate);
        sb.append(", UpdateDate=").append(UpdateDate);
        sb.append(", VistiCounts=").append(VistiCounts);
        sb.append(", SaleCounts=").append(SaleCounts);
        sb.append(", ShowSaleCounts=").append(ShowSaleCounts);
        sb.append(", DisplaySequence=").append(DisplaySequence);
        sb.append(", ImageUrl1='").append(ImageUrl1).append('\'');
        sb.append(", ImageUrl2='").append(ImageUrl2).append('\'');
        sb.append(", ImageUrl3='").append(ImageUrl3).append('\'');
        sb.append(", ImageUrl4='").append(ImageUrl4).append('\'');
        sb.append(", ImageUrl5='").append(ImageUrl5).append('\'');
        sb.append(", ThumbnailUrl40='").append(ThumbnailUrl40).append('\'');
        sb.append(", ThumbnailUrl60='").append(ThumbnailUrl60).append('\'');
        sb.append(", ThumbnailUrl100='").append(ThumbnailUrl100).append('\'');
        sb.append(", ThumbnailUrl160='").append(ThumbnailUrl160).append('\'');
        sb.append(", ThumbnailUrl180='").append(ThumbnailUrl180).append('\'');
        sb.append(", ThumbnailUrl220='").append(ThumbnailUrl220).append('\'');
        sb.append(", ThumbnailUrl310='").append(ThumbnailUrl310).append('\'');
        sb.append(", ThumbnailUrl410='").append(ThumbnailUrl410).append('\'');
        sb.append(", MarketPrice=").append(MarketPrice);
        sb.append(", BrandId=").append(BrandId);
        sb.append(", MainCategoryPath='").append(MainCategoryPath).append('\'');
        sb.append(", ExtendCategoryPath='").append(ExtendCategoryPath).append('\'');
        sb.append(", ExtendCategoryPath1='").append(ExtendCategoryPath1).append('\'');
        sb.append(", ExtendCategoryPath2='").append(ExtendCategoryPath2).append('\'');
        sb.append(", ExtendCategoryPath3='").append(ExtendCategoryPath3).append('\'');
        sb.append(", ExtendCategoryPath4='").append(ExtendCategoryPath4).append('\'');
        sb.append(", HasSKU=").append(HasSKU);
        sb.append(", IsfreeShipping=").append(IsfreeShipping);
        sb.append(", TaobaoProductId=").append(TaobaoProductId);
        sb.append(", ReferralDeduct=").append(ReferralDeduct);
        sb.append(", SubMemberDeduct=").append(SubMemberDeduct);
        sb.append(", SubReferralDeduct=").append(SubReferralDeduct);
        sb.append(", ShippingTemplateId=").append(ShippingTemplateId);
        sb.append(", Volume=").append(Volume);
        sb.append(", SecondLevelDeduct=").append(SecondLevelDeduct);
        sb.append(", ThreeLevelDeduct=").append(ThreeLevelDeduct);
        sb.append('}');
        return sb.toString();
    }
}
