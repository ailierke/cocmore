var LIMIT = 26;var editWin;var mbszWin;var excel;var jsonStr=[];

//上传图片类型  
var img_reg = /\.([jJ][pP][gG]){1}$|\.([jJ][pP][eE][gG]){1}$|\.([gG][iI][fF]){1}$|\.([pP][nN][gG]){1}$|\.([bB][mM][pP]){1}$/;

//create the data store
var records = new Ext.data.Record.create([  
       {name: 'fid'},
       {name: 'groupid'},
       {name: 'ispass'},
       {name: 'content'},
       {name: 'fnumber'},
       {name: 'ybasicMember'},
       {name: 'ybasicCity'},
       {name: 'ybasicTrade'},
       {name: 'ybasicCounty'},
       {name: 'ybasicProvince'},
       {name: 'fheadline'},
       {name: 'fimages'},
       {name: 'fmessage'},
       {name: 'ftel'},
       {name: 'fnationalCertification'},
       {name: 'fareGuarantee'},
       {name: 'fcontacts'},
       {name: 'fexpireTime'},
       {name: 'fauditTime'},
       {name: 'fpublisherTime'},
       {name: 'fauditIdea'},
       {name: 'fbillState'},
       {name: 'fcomment'},
       {name: 'fisHide'},
       {name: 'flevel'},
       {name: 'flag'}
]);
var store = new Ext.data.Store({
	reader:new Ext.data.JsonReader({
		root:'object',
		totalProperty:'count'
	},records),
	proxy:new Ext.data.HttpProxy({
			disableCaching :false,
			url: getPath() + '/supply/getYBasicAssurancecontentVo'
	}),
	remoteStore : true, // 是否远程调用数据
	remoteSort : true   // 是否远程排序
});
store.on("beforeload", function() {
	this.baseParams = {
			start:0,
			limit:LIMIT,
			groupId : window.parent.groupId
	};
});
var tool = new Ext.Toolbar({ //工具栏
	items:['状态：',{
   			xtype:'combo',
   			width:100,
			id:'searchIsPass', 
			typeAhead: true,
			triggerAction: 'all',
			lazyRender:true,
		    mode: 'local', 
		  	store: new Ext.data.ArrayStore({
		          fields: [
		              'myId',
		              'displayText'
		          ],
		          data: [['','全部'], [15, '待审核'],[16, '通过'],[17, '拒绝']]
		      }),
		     valueField: 'myId',
		     displayField: 'displayText',
		     renderer:function(value){
		      	return value.displayText;
		     }
		},'-']
});
Ext.Ajax.request({
	url : getPath() + 'systemconfigurationMenu/getfunctionjs',
	method : 'GET',
	params : {
		functionId : 34
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
    store.load({params:{start:0,limit:LIMIT,groupId:window.parent.groupId}});
    
    var cm = new Ext.grid.ColumnModel([
    	new Ext.grid.RowNumberer(),
    	{id:'fid',hidden:true},{
   			id:'fnumber', 
   			header: '编号', 
   			width: 150, 
   			hidden: true, 
   			dataIndex:'fnumber'
    	},{
   			id:'fheadline', 
   			header: '供应标题', 
   			width: 150, 
   			sortable: true, 
   			dataIndex:'fheadline'
    	},{
    		id:'content',
    		dataIndex:'content',
    		header:'担保内容'
    	},{
    	   	id:'ybasicSocialgroups',
    	   	dataIndex:'ybasicSocialgroups',
 	   		header: '社会团体', 
			hidden: true
       },{
    	   	id:'ybasicCity',
   	   		dataIndex:'ybasicCity',
	   		header: '城市', 
			hidden: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicTrade',
   	   		dataIndex:'ybasicTrade',
	   		header: '行业', 
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicCounty',
   	   		dataIndex:'ybasicCounty',
	   		header: '区县', 
			hidden: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	   	id:'ybasicProvince',
    	   	dataIndex:'ybasicProvince',
	   		header: '省份', 
			hidden: true,
			renderer : function(value) {
				if(value){
					return value.fname;
				}else{
					return "";
				}
			}
       },{
    	  	id:'fimages',
	   	   	header:'图片',
	   	   	width:150,
	   	   	hidden:true,
	   	   	dataIndex:'fimages'
      },{
	   		id:'fmessage', 
	   		header: '供应信息', 
	   		hidden: true, 
	   		dataIndex:'fmessage'
      },{
   	   		id:'ftel', 
   	   		header: '联系电话', 
  			width: 150, 
  			sortable: true, 
  			dataIndex:'ftel' 
      },{
    	   	id:'fnationalCertification', 
   	   		header: '国家认证', 
  			width: 150, 
  			hidden: true, 
  			dataIndex:'fnationalCertification'
       },{
    	   	id:'fareGuarantee', 
  	   		header: '是否担保', 
 			width: 150, 
 			hidden: true, 
 			dataIndex:'fareGuarantee',
 			renderer:function(value){
    			if(value==0){
    				return '否';
    			}else
    				return '是';
    		}
       },{
	   	   	id:'ybasicMember',
		   	dataIndex:'ybasicMember',
		   		header: '发布人', 
				width: 150, 
				sortable: true, 
				renderer : function(value) {
					if(value == null || value.fname==null){
						return "null";
					}
	            return value.fname;
	        }
       },{
    	   	id:'fcontacts', 
 	   		header: '联系人', 
			width: 150, 
			sortable: true, 
			dataIndex:'fcontacts'
       },{
   	   		id:'fauditTime', 
   	   		header: '审核时间', 
   	   		width: 150, 
   	   		hidden: true, 
   	   		dataIndex:'fauditTime'
       },{
   	   		id:'fpublisherTime', 
   	   		header: '发布时间', 
   	   		width: 150, 
   	   		hidden: true, 
   	   		dataIndex:'fpublisherTime'
       },{
    	   	id:'fexpireTime', 
	   		header: '到期时间', 
			width: 150, 
			hidden: true, 
			dataIndex:'fexpireTime'
       },{
    	   	id:'fauditIdea', 
	   		header: '审核意见', 
			width: 150, 
			hidden: true, 
			dataIndex:'fauditIdea'
       },{
   	   		id:'fisHide', 
   	   		header: '是否隐藏', 
   	   		width: 150, 
   	   		hidden: true, 
   	   		dataIndex:'fisHide',
   	   		renderer:function(value){
   	   			if(value==0){
   	   				return '否';
   	   			}else
   	   				return '是';
   	   		}
       },{
    		id:'flevel', 
    		header: '等级', 
    		width: 70,
    		dataIndex:'flevel'
       },{
    	   	id:'fcomment', 
   			header: '备注', 
   			width: 70, 
   			hidden: true,
   			dataIndex:'fcomment'
       },{
    	   id : 'fbillState',
           header : '状态',
           width : 70,
           hidden : true,
           renderer : status,
           dataIndex : 'fbillState'
       },{
    	   header:'状态',
    	   id:'ispass',
    	   dataIndex:'ispass',
    	   renderer:status
       },{id:'flag', dataIndex : 'flag',hidden:true}
    ]);
    function status(data){
    	if(data == 15){
    		return '<span style="color:blue">待审核</span>';
    	}else if(data == 16){
    		return '通过';
    	}else if(data == 17){
    		return '<span style="color:red">拒绝</span>';
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
    	tbar: tool,
		bbar: pagingToolBar
    });
    	
    if(getUser()[2]==1){
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	width:300,
        	items:[treePanelTuanti,grid]
        });
    }else{
    	new Ext.Viewport({
        	renderTo:'mainDiv',
        	id:'viewport',
        	layout:'border',
        	items:[grid]
        });
    }
    Ext.EventManager.onWindowResize(function(){ 
    	grid.getView().refresh(); 
	}); 
	grid.on('dblclick', editData);
});
/**************************** 自定义元素 **********************************/


var myImage1 = new Ext.BoxComponent({
    id : 'browseImage1',
    autoEl : {
        width : 60,
        height : 72,
        tag : 'img',
        src : Ext.BLANK_IMAGE_URL,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        complete : 'off',
        id : 'photo1'
    }
});
var myImage2 = new Ext.BoxComponent({
    id : 'browseImage2',
    autoEl : {
        width : 60,
        height : 72,
        tag : 'img',
        src : Ext.BLANK_IMAGE_URL,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        complete : 'off',
        id : 'photo2'
    }
});
var myImage3 = new Ext.BoxComponent({
    id : 'browseImage3',
    autoEl : {
        width : 60,
        height : 72,
        tag : 'img',
        src : Ext.BLANK_IMAGE_URL,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        complete : 'off',
        id : 'photo3'
    }
});
var myImage4 = new Ext.BoxComponent({
    id : 'browseImage4',
    autoEl : {
        width : 60,
        height : 72,
        tag : 'img',
        src : Ext.BLANK_IMAGE_URL,
        style : 'filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale);',
        complete : 'off',
        id : 'photo4'
    }
});

var editForm = new Ext.form.FormPanel({
    border : true,
    id:'editZzflForm',
	labelAlign: 'left',
	autoScroll:false,
    bodyStyle:'padding:5px',
    labelWidth:60,
	frame	: true,
	containerScroll: true,
	width: 700,
    height: 380,
    items:[{
   	 	layout:'column',
        items:[{
	    	columnWidth:.33,
	 	    layout: 'form',
		    border:false,
		    defaults:{ anchor:'91%',xtype:'textfield'},
		    items:[{xtype:'hidden',id:'fid',name:'fid'},
			        {fieldLabel:'编号',id:'fnumber',name:'fnumber',readOnly:true},
			        {fieldLabel:'标题',id:'fheadline',name:'fheadline',readOnly:true},
			        {fieldLabel : '行业',id : 'ybasicTrade',name : 'YBasicTrade.fid',readOnly:true},
			        {fieldLabel : '省份',id : 'ybasicProvince',name : 'YBasicProvince.fid',readOnly:true},
			        {fieldLabel : '城市',id : 'ybasicCity',name : 'YBasicCity.fid',readOnly:true},
			        {fieldLabel : '区县',id : 'ybasicCounty',name : 'YBasicCounty.fid',readOnly:true},
			        {fieldLabel:'社会团体',id:'ybasicSocialgroups',name:'YBasicSocialgroups.fid',xtype: 'hidden'}
				]
	   },{
   	    columnWidth:.34,
   	    layout: 'form',
   	    border:false,
		defaults:{ anchor:'90%',xtype:'textfield'},
   	    items: [
		        {fieldLabel: '审核时间',id:'fauditTime',name:'fauditTime',readOnly:true},
		        {fieldLabel: '发布时间',id:'fpublisherTime',name:'fpublisherTime',readOnly:true},
		        {fieldLabel: '到期时间',id:'fexpireTime',name:'fexpireTime',readOnly:true},
			   	{fieldLabel:'等级',id:'flevel',name:'flevel',readOnly:true},
		        {fieldLabel:'是否担保',id:'fareGuarantee',xtype:'radiogroup',readOnly:true,layout:'column',columns:2,
   				 	items:[{boxLabel: "是", name: 'fareGuarantee',inputValue: 1},
   				 	       {boxLabel: "否", name: 'fareGuarantee',inputValue: 0,checked:true}]
   	   	        },
   	   	        {fieldLabel:'担保项',id:'content',name:'content',readOnly:true},
			   	{fieldLabel:'审核意见',id:'fauditIdea',name:'fauditIdea',maxLength:125,xtype:"hidden"},
			   	{fieldLabel:'状态',id:'fbillState',name:'fbillState',xtype: 'hidden'},
			   	{fieldLabel:'自动增长',id:'flag',name:'flag',xtype: 'hidden'}
	   	    ]
	   },{
	   	    columnWidth:.33,
	   	    layout: 'form',
	   	    border:false,
			defaults:{ anchor:'90%',xtype:'textfield'},
	   	    items: [{fieldLabel:'图片',id:'fimages',name:'fimages',maxLength:125,xtype:'hidden'},
	   	   	        {fieldLabel:'联系人',id:'fcontacts',name:'fcontacts',maxLength:24,readOnly:true},
	   	   	        {fieldLabel:'联系电话',id:'ftel',name:'ftel',regex : /^((1+\d{10})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/,readOnly:true},
	   	   	        {fieldLabel:'国家认证',id:'fnationalCertification',name:'fnationalCertification',maxLength:25,readOnly:true},
			        {fieldLabel:'发布人',id : 'ybasicMember',name : 'YBasicMember.fid',readOnly:true},
					{fieldLabel:'是否隐藏',id:'fisHide',xtype:'radiogroup',readOnly:true,layout:'column',columns:2,
		   	        	items:[{boxLabel: "是", name: 'fisHide',inputValue: 1},
					 	       {boxLabel: "否", name: 'fisHide',inputValue: 0,checked:true}]
				   	},
			        {fieldLabel:'备注',id:'fcomment',name:'fcomment',maxLength:125,readOnly:true}
	   			   	 ]
		   }]
    },{
		 layout:'column',
		 height:100,
         column:4,
         items:[{
		        	columnWidth: .25,
					layout : "form",
					defaults : {anchor : '100%',xtype : 'textfield'},
		            items:[myImage1]
			   	},{
	                columnWidth : .25,
					layout : "form",
					defaults : {anchor : '100%',xtype : 'textfield'},
	                items:[myImage2]
			    },{
		            columnWidth : .25,
					layout : "form",
					defaults : {anchor : '100%',xtype : 'textfield'},
		            items:[myImage3]
			    },{
		            columnWidth : .25,
					layout : "form",
					defaults : {anchor : '100%',xtype : 'textfield'},
		            items:[myImage4]
		  		}]
    },{fieldLabel:'供应信息',id:'fmessage',name:'fmessage',xtype:'htmleditor',height:100,width:600,readOnly:true}]
});
	
function searchBy(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	//通过id获取输入的数据
	var searchCondition = Ext.getCmp("searchIsPass").getValue();
	//模糊查询
	if(searchCondition !=0){
		store.load({params:{ispass:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
	}else{
		store.reload({params:{ispass:searchCondition,groupId:window.parent.groupId,start:0,limit:LIMIT}});
	}
}

function editData(){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	var row = Ext.getCmp("grid").getSelectionModel().getSelected();
	if(!row){
	    	 alertWarring("请选择需要查看详情的供应信息!");return;
	}
    	this.row = row;
         if(!editWin){
            editWin = new Ext.Window({
            	id: 'editWin',
		        layout:'fit',
                width:700,
                height:410,
                frame:true,
                plain: true,
		        closeAction:'hide',
				maximizable:false,
				resizable:false,
				modal:true,
				border : false,
			    items :[editForm],
			    listeners:{
			    	hide:function(){
				    	editForm.form.reset();	
				    	Ext.getCmp('browseImage1').getEl().dom.src =Ext.BLANK_IMAGE_URL;
				    	Ext.getCmp('browseImage2').getEl().dom.src =Ext.BLANK_IMAGE_URL;
				    	Ext.getCmp('browseImage3').getEl().dom.src =Ext.BLANK_IMAGE_URL;
				    	Ext.getCmp('browseImage4').getEl().dom.src =Ext.BLANK_IMAGE_URL;
		    		}
			    }
            });
		}
        editWin.setTitle('查看供应信息详情');      
	    editForm.getForm().findField('fbillState').setValue(0);
	    loadData(row);
	    editWin.show();
	}

function loadData(row) {
	if(row.get('ybasicMember') && row.get('ybasicMember').fname){
		editForm.form.setValues({ybasicMember : row.get('ybasicMember').fname});
	}
	if(row.get('ybasicCity') && row.get('ybasicCity').fname){
		editForm.form.setValues({ybasicCity : row.get('ybasicCity').fname});
	}
	if(row.get('ybasicTrade') && row.get('ybasicTrade').fname){
		editForm.form.setValues({ybasicTrade : row.get('ybasicTrade').fname});
	}
	if(row.get('ybasicCounty') && row.get('ybasicCounty').fname){
		editForm.form.setValues({ybasicCounty : row.get('ybasicCounty').fname});
	}
	if(row.get('ybasicProvince') && row.get('ybasicProvince').fname){
		editForm.form.setValues({ybasicProvince : row.get('ybasicProvince').fname});
	}
	editForm.form.setValues({
		fid 					: row.get('fid'),
		content					: row.get('content'),
		fnumber  				: row.get('fnumber'),
		fheadline				: row.get('fheadline'),
		fauditTime				: row.get('fauditTime'),
		fpublisherTime			: row.get('fpublisherTime'),
		fexpireTime				: row.get('fexpireTime'),
		fimages					: row.get('fimages'),
		fmessage				: row.get('fmessage'),
		ftel					: row.get('ftel'),
		fnationalCertification	: row.get('fnationalCertification'),
		fareGuarantee			: row.get('fareGuarantee'),
		fcontacts				: row.get('fcontacts'),
		fisHide					: row.get('fisHide'),
		flevel					: row.get('flevel'),
		fauditIdea				: row.get('fauditIdea'),
		fcomment				: row.get('fcomment'),
		fbillState				: 10,
		flag					: row.get('flag')
	});

	var imgLength = row.get('fimages').split(',');
    for(var i =0;i<imgLength.length;i++){
   	 if(i==imgLength.length-1)return;
//        photoSrc(imgLength[i],(i+1));
    }
}

//==============================修改状态================================
function updateReplyState(stateId){
	if(checkLoginTimeOut()){ alertWarring("登陆已超时! 点击<a href='javascript:void(0)' onclick='clickHere()'>这里</a>"+"重新登录");return;}
	if(!window.parent.groupId){ alertWarring("请先选择团体，再进行操作");return;}
	if(Ext.getCmp('grid').getSelectionModel().hasSelection()){
		  var records=Ext.getCmp('grid').getSelectionModel().getSelections();
		  var fids=new Array();
		  for(var i=0;i<records.length;i++){
			  fids[i]=records[i].get('fid');
		  }
		 Ext.MessageBox.show({
		    title: '提示',
		    msg: "确认要更新此供应 担保 状态？",
		    width:250,
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
			msg: "请选择担保内容!",
			width: 150,
			buttons: Ext.MessageBox.OK,
			buttonAlign: 'center'
		});
	}
}

function gridSubAjax(param,stateId)
{
	Ext.Ajax.request({
		url : getPath() + '/supply/updateYBasicAssurancecontent',
		params : {supplygroupId : param , ispass : stateId},
		success: function (result, request){
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

//图片上传成功 给产品图片文本，预览img赋值
function photoSrc(src,index){
	var a = Ext.getCmp('browseImage'+index);
    if(Ext.isIE){
        var image = Ext.getCmp('browseImage'+index).getEl().dom;
        image.src = Ext.BLANK_IMAGE_URL;
        image.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod = scale)";
        image.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = src;
    }else{ // 支持FF
        Ext.getCmp('browseImage'+index).getEl().dom.src =src;
    }
}

treePanel2.on('click',function(n){
	window.parent.groupId = n.attributes.id;
	store.load();
});