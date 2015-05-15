var LIMIT = 26;var editWin;var mbszWin;var superId=0;var excel;

var records = new Ext.data.Record.create([
                                          {name: 'fid'},
                                          {name: 'fnumber'},
                                          {name: 'fname'},
                                          {name: 'fabbreviation'},
                                          {name: 'fenglishName'},
                                          {name: 'fdescribe'},
                                          {name: 'fstartTime'},
                                          {name: 'fstorageTime'},
                                          {name: 'falterDescribe'},
                                          {name: 'faddress'},
                                          {name: 'fsuperiorOrganizationId'},
                                          {name: 'fgradationType'},
                                          {name: 'fphone'},
                                          {name: 'funitGradation'},
                                          {name: 'ffax'},
                                          {name: 'fdistrictId'},
                                          {name: 'femail'},
                                          {name: 'fpostcode'},
                                          {name: 'festablishDate'},
                                          {name: 'fregisteredCapital'},
                                          {name: 'fregisterNumber'},
                                          {name: 'forganizationCode'},
                                          {name: 'fbusinessIndate'},
                                          {name: 'ftaxAffairsNumber'},
                                          {name: 'fchurchyard'},
                                          {name: 'foverseas'},
                                          {name: 'flegalPersonCorporation'},
                                          {name: 'flegalPersonDelegate'},
                                          {name: 'fleafNode'},
                                          {name: 'flevel'},
                                          {name: 'fbillState'},
                                          {name: 'fcomment'},
                                          {name: 'fcreaterId'},
                                          {name: 'fmodifiedId'},
                                          {name: 'flastModifiedId'},
                                          {name: 'fcreateTime'},
                                          {name: 'fmodifiedTime'},
                                          {name: 'flastModifiedTime'},
                                          {name: 'flag'}
                                       ]
                                   );

var store = new Ext.data.Store({
   	reader:new Ext.data.JsonReader({
   		root:'obj.list',
   		totalProperty:'obj.count'
   	},records),
   	proxy:new Ext.data.HttpProxy({
   			disableCaching :false,
			url: getPath() + '/org/findAllOrg'
	}),
   	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});
var tool = new Ext.Toolbar({ //工具栏
	items:['名称：',{
		xtype:'textfield',
		emptyText:'请输入名称',
		id:'searchCondition',
		width:150 
	},'-']
});

Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 26
	},
	success : function(response, options) {
		obj=Ext.util.JSON.decode(response.responseText);
		for(var i=0;i<obj.length;i++){
			tool.add(Ext.util.JSON.decode(obj[i]));tool.add('-');
		}
		tool.doLayout();
	}
});
Ext.onReady(function(){
    Ext.QuickTips.init();
    Ext.form.Field.prototype.msgTarget = 'side';
    
    
    store.on('load', function(store, records, options) {
        if (records.length = 0) {
            Ext.Msg.alert("提示信息", "没有符合条件的数据!"); 
        }
    });
    store.load({params:{start:0,limit:LIMIT}});
    
    var order_sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},
    	{id:'flag',hidden:true},
    	{
    		id:'fnumber', 
    		header: '编号', 
    		width: 150, 
    		hidden: true, 
    		dataIndex:'fnumber'
       },{
    		id:'fname', 
    		header: '名称', 
    		width: 150, 
    		sortable: true, 
    		dataIndex:'fname'
       },{
    		id:'fabbreviation', 
    		header: '简称', 
    		width: 80, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fabbreviation'
       },{
    		id:'fenglishName', 
    		header: '英文名称', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fenglishName'
       },{
    		id:'fdescribe', 
    		header: '描述', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fdescribe'
       },{
   		id:'fstartTime', 
		header: '开始时间', 
		width: 150, 
		sortable: true, 
		allowBlank: true,
		dataIndex:'fstartTime',
		hidden:true
       },{
  		id:'fstorageTime', 
		header: '封存时间', 
		width: 150, 
		sortable: true, 
		allowBlank: true,
		dataIndex:'fstorageTime',
		hidden:true
       },{
    		id:'falterDescribe', 
    		header: '变更描述', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'falterDescribe',
    		hidden:true
       },{
    		id:'faddress', 
    		header: '地址', 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'faddress'
       },{
    		id:'fsuperiorOrganizationId', 
    		header: '上级行政组织', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fsuperiorOrganizationId',
    		hidden:true
       },{
    		id:'fgradationType', 
    		header: '组织层次类型', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'fgradationType',
    		hidden:true
       },{
	   		id:'fphone', 
			header: '电话', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fphone'
	   },{
	   		id:'funitGradation', 
			header: '组织单元层次', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'funitGradation',
    		hidden:true
	   },{
			id:'ffax', 
			header: '传真', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'ffax',
    		hidden:true
	   },{
			id:'fdistrictId', 
			header: '区域', 
			width: 70, 
			sortable: true,
			renderer:status,
			dataIndex:'fdistrictId',
    		hidden:true
	   },{
			id:'femail', 
			header: '邮箱', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'femail'
	   },{
	   		id:'fpostcode', 
			header: '经济类型', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fpostcode',
    		hidden:true
	   },{
			id:'fpostcode', 
			header: '邮编', 
			width: 70, 
			sortable: true,
			dataIndex:'fpostcode'
	   },{
	   		id:'festablishDate', 
			header: '成立日期', 
			width: 150, 
			sortable: true, 
			dataIndex:'festablishDate',
    		hidden:true
	   },{
			id:'fregisteredCapital', 
			header: '注册资本', 
			width: 150, 
			sortable: true, 
			dataIndex:'fregisteredCapital',
    		hidden:true
	   },{
			id:'fregisterNumber', 
			header: '注册登记号', 
			width: 70, 
			sortable: true,
			renderer:status,
			dataIndex:'fregisterNumber',
    		hidden:true
	   },{
			id:'forganizationCode', 
			header: '组织机构代码', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'forganizationCode',
    		hidden:true
	   },{
	   		id:'fbusinessIndate', 
			header: '营业有效期', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fbusinessIndate',
    		hidden:true
	   },{
			id:'ftaxAffairsNumber', 
			header: '税务号', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'ftaxAffairsNumber',
    		hidden:true
	   },{
			id:'fchurchyard', 
			header: '境内', 
			width: 70, 
			sortable: true,
			dataIndex:'fchurchyard',
    		hidden:true
	   },{
    		id:'foverseas', 
    		header: '境外', 
    		width: 150, 
    		sortable: true, 
    		allowBlank: true,
    		dataIndex:'foverseas',
    		hidden:true
       },{
	   		id:'flegalPersonCorporation', 
			header: '法人公司', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'flegalPersonCorporation',
    		hidden:true
	   },{
			id:'flegalPersonDelegate', 
			header: '法人代表', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'flegalPersonDelegate'
	   },{
    		id:'fleafNode', 
    		header: '是否为叶子节点', 
    		width: 70, 
    		sortable: true,
    		dataIndex:'fleafNode',
    		hidden:true
       },{
	   		id:'flevel', 
			header: '级次', 
			width: 150, 
			sortable: true, 
			dataIndex:'flevel',
    		hidden:true
	   },{
			field : 'fcomment',
			header : '备注',
			width : 70,
			allowBlank: true,
			dataIndex:'fcomment'
	   },{
	   		id:'fcreaterId', 
			header: '创建人', 
			width: 100, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fcreaterId',
    		hidden:true
	   },{
			id:'fcreateTime', 
			header: '创建时间', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fcreateTime',
    		hidden:true
	   },{
	   		id:'fmodifiedId', 
			header: '修改人', 
			width: 100, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fmodifiedId',
    		hidden:true
	   },{
			id:'fmodifiedTime', 
			header: '修改时间', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'fmodifiedTime',
    		hidden:true
	   },{
	   		id:'flastModifiedId', 
			header: '最后修改人', 
			width: 100, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'flastModifiedId',
    		hidden:true
	   },{
			id:'flastModifiedTime', 
			header: '最后修改时间', 
			width: 150, 
			sortable: true, 
			allowBlank: true,
			dataIndex:'flastModifiedTime',
    		hidden:true
	   },{
			id:'fbillState', 
			header: '状态', 
			width: 70, 
			sortable: true,
			renderer:status,
			dataIndex:'fbillState'
	   }
    ]);
    function status(data){
    	if(data == 0){
    		return '新增';
    	}else if(data == 1){
    		return '保存';
    	}else if(data == 2){
    		return '提交';
    	}else if(data == 3){
    		return '审核';
    	}else if(data == 4){
    		return '反审核';
    	}else if(data == 5){
    		return '生效';
    	}else if(data == 6){
    		return '<span style="color:red">失效</span>';
    	}else if(data == 7){
    		return '启用';
    	}else if(data == 8){
    		return '禁用';
    	}else if(data == 9){
    		return '<span style="color:red">作废</span>';
    	}else if(data == 10){
    		return '变更';
    	}
	}
    var pagingToolBar = new Ext.PagingToolbar({
    	pageSize:LIMIT,
    	store:store,
    	displayInfo: true,
    	displayMsg: '当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条',
    	emptyMsg: "没有记录",
		plugins: new Ext.ux.ProgressBarPager()
	}); 
    
    // create the Grid
    var grid = new Ext.grid.GridPanel({
    	region : 'center', 
    	id:'grid',    		
    	border: false,
    	columnLines:true,
    	loadMask:true,
    	store: store,
    	frame:false,
    	viewConfig: {forceFit:true},
    	monitorResize: true,
    	cm: cm,
    	tbar:tool,
		bbar: pagingToolBar
    });
    
    var root = new Ext.tree.AsyncTreeNode({
    	draggable:false,
		id : "source",
		text : "集团",
		loader: new Ext.tree.TreeLoader({dataUrl:getPath() + '/org/getOrgTree'})
    });
    var treePanel = new Ext.tree.TreePanel({
    	region : 'west',
		width : 200, 
    	title:'组织结构',
		root : root,
		rootVisible : false,
		autoScroll : true,
		monitorResize: true,
		listeners:{
    		click:function(n){
    			window.parent.orgId = n.attributes.id;
    			//storeEmp.load({params:{orgId:n.attributes.id,start:0,limit:LIMIT}});
    		}
      	}
	});
    var displayPanel = new Ext.Viewport({
    	renderTo:'mainDiv',
    	layout:'border',
    	items:[treePanel,grid]
    });
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh() 
	}) 
	grid.on('dblclick', editData);
});
/**************************** 自定义元素 **********************************/
//excel模板设置
function mbsz(){
	var export_url = getPath() + '/base/organization!doNotNeedSessionAndSecurity_Download.ad';
    window.location.href = export_url;
}
//导入
/*导入数据功能*/
function showImportExcel(){
	if(!excel){
		excel = new Ext.Window({
	        id: 'showImportExcel',
	        layout:'fit',
            width:300,
            height:140,
            frame:true,
            plain: true,
	        closeAction:'hide',
			maximizable:false,
			resizable:false,
			modal:true,
			border:false,
		    items :[ importExcelForm ]
        });
	}
	Ext.getCmp('showImportExcel').show();
} 

var importExcelForm= new Ext.form.FormPanel({
	frame: true,
	fileUpload: true, 
	waitMsgTarget : true,
	labelAlign : 'left',
	enctype:'multipart/form-data', 
	bodyStyle : 'padding:5px 0px 0',
	buttonAlign : 'center',
	id : 'uploadFileLogo',
	items : [
		new Ext.ux.form.FileUploadField({
	    	id : 'flshlogo',
	    	name : 'methohdspath',
	    	emptyText: '选择xls文件',
			width : 150,
			buttonText: '浏览...'
		})
	],
    buttons:[{
	    text: '上传',
	    handler:function(){
	    	 var logourl = Ext.getCmp('flshlogo').getValue();
    		if(Ext.getCmp('uploadFileLogo').getForm().isValid() && /\.(xls)$/.test(logourl)){
    			Ext.getCmp('uploadFileLogo').getForm().submit({
		                   method:'POST',
		                   waitMsg:'正在提交.....',
						   waitTitle:'请稍等',
						   url : getPath() + '/base/organization!doNotNeedSessionAndSecurity_import.ad',
		                   success:function(form, action){
		                	    location.reload();
		                	    Ext.getCmp("showImportExcel").hide();
		                   },
		                   failure:function(form, action){
		                	   alertWarring(action.result.msg);
		                   }       
		              });  
			}else{
				alertWarring("请选择后缀名为xls的文件");
			}
		}
    }] 
});

/*******导出数据******/
function exprotData(){
	var vExportContent = Ext.getCmp('grid').getExcelXml();  
	if (Ext.isIE9||Ext.isIE8||Ext.isIE6 || Ext.isIE7 || Ext.isSafari|| Ext.isSafari2 || Ext.isSafari3){ //判断浏览器
        var fd = Ext.get('frmDummy');
        if (!fd) {
            fd = Ext.DomHelper.append(
                    Ext.getBody(), {
                        tag : 'form',
                        method : 'post',
                        id : 'frmDummy',
                        action : getPath()+'/exportUrl.jsp',
                        target : '_blank',
                        name : 'frmDummy',
                        cls : 'x-hidden',
                        cn : [ {
                            tag : 'input',
                            name : 'exportContent',
                            id : 'exportContent',
                            type : 'hidden'
                        } ]
                    }, true);
            
        }
        fd.child('#exportContent').set( {
            value : vExportContent
        });
        fd.dom.submit();
    } else {
    	document.location = 'data:application/vnd.ms-excel;base64,' + Base64.encode(vExportContent);
	}
}

/**************************** 自定义元素结束 *******************************/
var FSuperiorOrganizationIDStore = new Ext.data.Store({
//	autoLoad:true,
	proxy : new Ext.data.HttpProxy({
		disableCaching :false,
		method:'post',
		url : getPath() + '/org/findAllOrgBySuperId'
	}),
	params:{superId:superId},
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
//上级行政组织
var FSuperiorOrganizationID = new Ext.form.ComboBox({
	fieldLabel : "上级行政组织",
	store : FSuperiorOrganizationIDStore,
	id:'FSuperiorOrganization',
	hiddenName : 'fsuperiorOrganizationId',
	triggerAction : 'all',
	lazyInit : true,
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	mode:'local',
	emptyText:'请选择上级行政组织'
});

//区域仓库
var fdistrictIdStore = new Ext.data.Store({
	autoLoad:true,
	proxy : new Ext.data.HttpProxy({
		disableCaching :false,
		method:'post',
		url : getPath() + '/dis/findAllDis'
	}),
	reader : new Ext.data.JsonReader({root:'obj'}, [{name : 'fid'}, {name : 'fname'}])
});
var districtCom = new Ext.form.ComboBox({
	fieldLabel : "区域",
	store : fdistrictIdStore,
	id:'fdistrictId',
	hiddenName : 'fdistrictId',
	triggerAction : 'all',
	lazyInit : true,
	valueField : 'fid',
	displayField : 'fname',
	selectOnFocus:true,
	editable:false,
	mode:'local',
	emptyText:'请选择所属区域'
});

//组织分类编辑框
var editForm = new Ext.form.FormPanel({
     border : false,
     id:'editZzflForm',
	 labelAlign: 'right',
	 autoScroll:false,
     baseCls:'x-plain',
     margins : '5 0 5 5',
     bodyStyle:'padding:5px',
     items:[{
    	 layout:'column',
         baseCls:'x-plain',
	     laberWidth:60,
         items:[{
	    	columnWidth:.5,
	 	    layout: 'form',
	 	    baseCls:'x-plain',
		    border:false,
		    defaults:{ anchor:'90%',xtype:'textfield'},
		    items: [{xtype:'hidden',name:'fid',id:'fid'},{
	                fieldLabel:'编号',
	                id:'fnumber',
	                name:'fnumber',
	                xtype:'hidden',
	                blankText:'该字段不允许为空',
	                allowBlank: false
	            },{
	                fieldLabel:'简称',
	                id:'fabbreviation',
	                name:'fabbreviation',
	                maxLength:24,
	                blankText:'该字段不允许为空',
	                allowBlank: false
	            },{
	                fieldLabel:'描述',
	                maxLength:125,
	                id:'fdescribe',
	                name:'fdescribe'
	            },FSuperiorOrganizationID,{
	                fieldLabel:'组织层次类型',
	                id:'fgradationType',
	                name:'fgradationType',
		            maxLength:2
	            },{
	                fieldLabel:'电话',
	                id:'fphone',
	                name:'fphone',
	                blankText:'该字段不允许为空',
	                allowBlank: false,
			        regex : /^((1[3|4|5|8]+\d{9})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
			        regexText:"格式不正确!" //正则表达式错误提示
	            },{
	                fieldLabel:'传真',
	                id:'ffax',
	                name:'ffax',
	                blankText:'该字段不允许为空',
	                allowBlank: false,
	                regex : /^((1[3|4|5|8]+\d{9})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,
			        regexText:"格式不正确!" 
	            },districtCom,{
	            	xtype:'datefield',
	                fieldLabel:'成立日期',
	                format:"Y-m-d H:i:s",
	                id:'festablishDate',
	                name:'festablishDate',
	                editable:false,
	                blankText:'该字段不允许为空',
	                allowBlank: false,
		            vtype : 'daterange',
		            endDateField : 'fbusinessIndate'
	            },{
	                fieldLabel:'注册登记号',
	                id:'fregisterNumber',
	                name:'fregisterNumber',
	                regex : /^(\d{8})-(\d{1})$/,
			        regexText:"格式不正确!" 
	            },{
	            	xtype:'datefield',
	                fieldLabel:'营业有效期',
	                format:"Y-m-d",
	                id:'fbusinessIndate',
	                name:'fbusinessIndate',
	                blankText:'该字段不允许为空',
	                allowBlank: false,
	                editable: false,
		            vtype : 'daterange',
	                startDateField : 'festablishDate'
	            },{
	            	xtype:'radiogroup',
				 	layout:'column',
	                fieldLabel:'境内',
	                id:'fchurchyard',
    	            columns:2,
				 	items:[{boxLabel: "是", name: 'fchurchyard',inputValue: "是",checked:true},  
				 	       {boxLabel: "否", name: 'fchurchyard',inputValue: "否"}]
	            },{
	                fieldLabel:'法人公司',
	                maxLength:24,
	                id:'flegalPersonCorporation',
	                name:'flegalPersonCorporation'
	            },{
	            	xtype:'datefield',
	                fieldLabel:'启动日期',
	                format:"Y-m-d H:i:s",
	                id:'fstartTime',
	                name:'fstartTime',
	                editable:false,
	                blankText:'该字段不允许为空',
	                allowBlank: false,
	                vtype : 'daterange',
		            endDateField : 'fstorageTime'
	            },{
	            	xtype:'datefield',
	                fieldLabel:'封闭日期',
	                format:"Y-m-d H:i:s",
	                id:'fstorageTime',
	                name:'fstorageTime',
	                editable:false,
	                blankText:'该字段不允许为空',
	                allowBlank: false  
	            }]
	   },{
    	    columnWidth:.5,
    	    layout: 'form',
    	    baseCls:'x-plain',
    	    border:false,
		    defaults:{ anchor:'90%',xtype:'textfield'},
    	    items: [
    	        {
    	            fieldLabel:'名称',
    	            id:'fname',
    	            name:'fname',
	                maxLength:24,
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
    	        },{
    	            fieldLabel:'英文名称',
    	            id:'fenglishName',
    	            name:'fenglishName',
    	            regex:/^[a-zA-Z]+$/,
    	            regexText:'格式不正确',
	                maxLength:48,
    	            blankText:'该字段不允许为空',
	                allowBlank: false
    	        },{
    	            fieldLabel:'变更描述',
	                maxLength:125,
    	            id:'falterDescribe',
    	            name:'falterDescribe'
    	        },{
    	            fieldLabel:'地址',
    	            id:'faddress',
    	            name:'faddress',
	                maxLength:24,
    	            blankText:'该字段不允许为空',
	                allowBlank: false
    	        },{
    	        	xtype:'numberfield',
    	            fieldLabel:'组织单元层次',
	                maxLength:2,
    	            id:'funitGradation',
    	            name:'funitGradation'
    	        },{
    	            fieldLabel:'邮箱',
    	            id:'femail',
    	            name:'femail',
	                maxLength:24,
    	            vtype:'email',
    	            blankText:'该字段不允许为空',
    	            allowBlank: false
    	        },{
    	            fieldLabel:'邮编',
    	            id:'fpostcode',
    	            name:'fpostcode',
    	            regex : /^[0-9]{6}$/,
			        regexText:"格式不正确!" 
    	        },{
    	        	xtype:'numberfield',
    	            fieldLabel:'注册资本',
    	            id:'fregisteredCapital',
    	            name:'fregisteredCapital'
    	        },{
    	            fieldLabel:'组织机构代码',
    	            id:'forganizationCode',
    	            name:'forganizationCode',
	                regex : /^\d{15}$/,
			        regexText:"格式不正确!" ,
			        listeners:{
			        	blur:function(){
			        		var fregisterNumber = Ext.getCmp('forganizationCode').getValue();
			        		if(fregisterNumber.length==15){
			        			var night = fregisterNumber.substring(6,fregisterNumber.length);
			        			night = night.substring(0,night.length-1)+'-'+night.substring(night.length-1, night.length);
				        		Ext.getCmp('fregisterNumber').setValue(night);
			        		}
			        	}
			        }
    	        },{
    	        	xtype:'numberfield',
    	            fieldLabel:'税务号',
    	            id:'ftaxAffairsNumber',
    	            name:'ftaxAffairsNumber',
	                maxLength:24,
    	            blankText:'该字段不允许为空',
	                allowBlank: false
    	        },{
    	        	xtype:'radiogroup',
				 	layout:'column',
    	            fieldLabel:'境外',
    	            id:'foverseas',
    	            columns:2,
				 	items:[{boxLabel: "是", name: 'foverseas',inputValue: "是"},  
				 	       {boxLabel: "否", name: 'foverseas',inputValue: "否",checked:true}]
    	        },{
    	            fieldLabel:'法人代表',
	                maxLength:24,
    	            id:'flegalPersonDelegate',
    	            name:'flegalPersonDelegate'
    	        },{
    	        	xtype:'radiogroup',
				 	layout:'column',
    	            fieldLabel:'叶子节点',
    	            id:'fleafNode',
    	            columns:2,
				 	items:[{boxLabel: "是", name: 'fleafNode',inputValue: "是"},  
				 	       {boxLabel: "否", name: 'fleafNode',inputValue: "否",checked:true}]
    	        },{
	                fieldLabel:'备注',
	                id:'fcomment',
	                name:'fcomment',
		            maxLength:125
	            },{
    		        xtype:'hidden',
    	            id:'flevel',
    	            name:'flevel'
    	        },{
    		        xtype:'hidden',
    		        id:'fbillState',
    		        name:'fbillState'
    	        },{
    		        xtype:'hidden',
    		        id:'fcreateTime',
    		        name:'fcreateTime'
    	        },{
    		        xtype:'hidden',
    		        id:'fcreaterId',
    		        name:'fcreaterId'
    	        },{
    	   		 xtype:'hidden',
    		     id:'flag',
    		     name:'flag'
    		 },{
    	   		 xtype:'hidden',
    		     id:'fmodifiedId',
    		     name:'fmodifiedId'
    		 },{
    	   		 xtype:'hidden',
    		     id:'fmodifiedTime',
    		     name:'fmodifiedTime'
    		 },{
    			 xtype:'hidden',
    		     id:'flastModifiedId',
    		     name:'flastModifiedId'
    		 },{
    			 xtype:'hidden',
    		     id:'flastModifiedTime',
    		     name:'flastModifiedTime'
    		 }
    	    ]
	    }]
	}]                  
});
/**************************** 操作 **********************************/

function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	var searchCondition = Ext.getCmp('searchCondition').getValue();
	//模糊查询
	store.load({params:{searchName:searchCondition,start:0,limit:LIMIT}});
}

//组织分类窗口
function editData(row){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(row!= 'add'){
		 row = Ext.getCmp('grid').getSelectionModel().getSelected();
		 if(!row){
	    	 alertWarring("请选择需要编辑的组织信息!");return;
	     }
	     var billState = row.get("fbillState");
	     if(billState == 7){
				alertError("该状态为生效状态，不能修改！");return;
		 }
	}else{
		getNumberStore();
	}
	this.row = row;
     if(!editWin){
        editWin = new Ext.Window({
	        id: 'editWin',
	        layout:'fit',
            width:600,
            height:460,
	        closeAction:'hide',
			maximizable:false,
			resizable:true,
			modal:true,
            frame:true,
		    items :[editForm],
		    buttons:[{
				    text: '保存',
				    handler:function(){
				    	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
				    	if(editForm.form.isValid()){
				        	  editForm.form.submit({
				                   method:'POST',
				                   waitMsg:'正在提交.....',
								   waitTitle:'请稍等',
				                   success:function(form, action){
				                	   Ext.getCmp("grid").store.reload({callback: function(records, options, success){
					    					//msgTip.hide(); // 加载完成，关闭提示框
					    				}});
				                  	   Ext.getCmp("editWin").hide();
				                	   editForm.form.reset();
				                   },
				                   failure:function(form, action){
				                	   alertWarring(action.result.msg);
				                   }
				              });   
				          }else{
				        	  alertWarring("内容填写不完整");
				          }
				     }
				 },{
					 text:'取消',
				     handler:function(){
				    	 Ext.getCmp("editWin").hide();
				     }
			 }],
		    listeners:{hide:function(){
		    editForm.form.reset();
		    FSuperiorOrganizationIDStore.load();
			editForm.form.reset();}}
        });
	}
    setTitleAndUrl(row, editWin); 
    getNumberStore();
	var date = new Date();  // 得到系统日期
    var dateString = date.getFullYear()+"-"+(date.getMonth()+1)+"-"+date.getDate()+" "+date.getHours()+":"+date.getMinutes()+":"+date.getSeconds();
    if(row != 'add') { 
    	editForm.getForm().findField('fmodifiedTime').setValue(dateString);
        editForm.getForm().findField('fmodifiedId').setValue(getUser()[1]);
        editForm.getForm().findField('flastModifiedTime').setValue(dateString);
        editForm.getForm().findField('flastModifiedId').setValue(getUser()[1]);
      	loadData(row);
	}else{
		editForm.getForm().findField('fcreateTime').setValue(dateString);
	    editForm.getForm().findField('fcreaterId').setValue(getUser()[1]);
	    editForm.getForm().findField('fbillState').setValue(0);
	}
}

function loadData(row) {
	superId = row.get('fid');
	FSuperiorOrganizationIDStore.reload({
		params:{superId:row.get('fid')},
		callback:function(){
			editForm.form.setValues({
				fid										: row.get("fid"),
				fnumber 							: row.get('fnumber'),
				fname    							: row.get('fname'),
				fabbreviation  					: row.get('fabbreviation'),
				fenglishName   					: row.get('fenglishName'),
				fdescribe  							: row.get('fdescribe'),
				fstartTime  						: row.get('fstartTime'),
				fstorageTime  					: row.get('fstorageTime'),
				falterDescribe 					: row.get('falterDescribe'),
				//forganizationType				: row.get('forganizationType'),
				faddress  							: row.get('faddress'),
				FSuperiorOrganization 		: row.get('fsuperiorOrganizationId'),
				fgradationType  				: row.get('fgradationType'),
				fphone  							: row.get('fphone'),
				funitGradation  					: row.get('funitGradation'),
				ffax 									: row.get('ffax'),
				fdistrictId 								: row.get('fdistrictId'),
				femail    							: row.get('femail'),
				//feconomicType  				: row.get('feconomicType'),
				fpostcode   						: row.get('fpostcode'),
				//findependentAccounting  	: row.get('findependentAccounting'),
				festablishDate  					: row.get('festablishDate'),
				fregisteredCapital  			: row.get('fregisteredCapital'),
				fregisterNumber 				: row.get('fregisterNumber'),
				forganizationCode				: row.get('forganizationCode'),
				fbusinessIndate  				: row.get('fbusinessIndate'),
				ftaxAffairsNumber 				: row.get('ftaxAffairsNumber'),
				fchurchyard  						: row.get('fchurchyard'),
				foverseas  						: row.get('foverseas'),
				flegalPersonCorporation 	: row.get('flegalPersonCorporation'),
				flegalPersonDelegate 		: row.get('flegalPersonDelegate'),
				fleafNode 							: row.get('fleafNode'),
				flevel  								: row.get('flevel'),
				fcomment  						: row.get('fcomment'),
			//	flegalPersonCorporation		: row.get('flegalPersonCorporation'),
				fcreateTime						:row.get('fcreateTime'),
				fcreaterId						:row.get('fcreaterId'),
				fbillState					:10,
				flag					:row.get('flag')
			});
		
	}});
}
function setTitleAndUrl(row, win) {
	if(row == 'add') {
 		win.setTitle('增加组织分类');
 		editForm.form.url = getPath()+'/org/saveOrg';
	} else {
 		win.setTitle('修改组织分类');
 		editForm.form.url = getPath()+'/org/updateOrg';
	}
	win.show();
}

function setState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		 }
		Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认需要更新当前组织状态!",
		    width:300,
		    buttons: Ext.MessageBox.YESNO,
		    fn: function(e) {
		    		if(e == 'yes') {
		    			gridSubAjax(fids,stateId);
		    		}
		    	},
		    icon : Ext.MessageBox.QUESTION
		});
	}
	else
	{
		Ext.MessageBox.show({
			title: '提示',
			msg: "请选择需要更新状态的组织信息!",
			width: 250,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}



function gridSubAjax(param,stateId)
{
	
	Ext.Ajax.request({
		url : getPath() + '/org/updateOrgStatus',
		params : {fids : param , status : stateId},
		
		success: function (result, request) 
		{
			//var msg = eval(result.responseText);
			Ext.MessageBox.show({
				title : "提示",
				msg : "更新成功！",
				width : 250,
				buttons : Ext.MessageBox.OK,
				icon : Ext.MessageBox.INFO
			});
			Ext.getCmp("grid").store.reload();
		},
		failure: function ( result, request) {
			alertError("系统运行超时或执行失败");
		}
	});
}


function getNumberStore(){
	Ext.Ajax.request({
		url:getPath() + '/serialNumber/getSerialNumber',
		params:{prefix:'JC-ZZGL'},
		success:function(response, options) {
			Ext.getCmp("fnumber").setValue(Ext.util.JSON.decode(response.responseText).serialNumber);
		}
	});
}