"use strict";(self["webpackChunkfont_end_vue"]=self["webpackChunkfont_end_vue"]||[]).push([[759],{6112:function(n,a,t){t.r(a),t.d(a,{default:function(){return $}});var e=t(3396),d=t(4870),r=t(7139);const l={class:"card"},c=(0,e._)("div",{class:"card-header"},"ChlidA",-1),o={class:"card-body"},u=(0,e._)("hr",null,null,-1);var s={props:["prop1","prop2","productNo","productKind"],setup(n){return(a,t)=>((0,e.wg)(),(0,e.iD)("div",l,[c,(0,e._)("div",o,[(0,e._)("div",null,[(0,e._)("p",null,"prop1: "+(0,r.zw)(a.$attrs.prop1),1),(0,e._)("p",null,"prop2: "+(0,r.zw)(a.$attrs.prop2),1),(0,e._)("p",null,"productNo: "+(0,r.zw)(a.$attrs.productNo),1),(0,e._)("p",null,"productKind: "+(0,r.zw)(a.$attrs["product-kind"]),1),(0,e._)("p",null,"productPrice: "+(0,r.zw)(a.$attrs.productPrice),1)]),u,(0,e._)("div",null,[(0,e._)("p",null,"prop1: "+(0,r.zw)(n.prop1),1),(0,e._)("p",null,"prop2: "+(0,r.zw)(n.prop2),1),(0,e._)("p",null,"productNo: "+(0,r.zw)(n.productNo),1),(0,e._)("p",null,"productKind: "+(0,r.zw)(n.productKind),1),(0,e._)("p",null,"productPrice: "+(0,r.zw)(a.productPrice),1)])])]))}};const i=s;var p=i;const v={class:"card"},m=(0,e._)("div",{class:"card-header"},"ChlidB",-1),_={class:"card-body"};var b={props:["no","kind"],setup(n){return(a,t)=>((0,e.wg)(),(0,e.iD)("div",v,[m,(0,e._)("div",_,[(0,e._)("p",null,"no: "+(0,r.zw)(n.no),1),(0,e._)("p",null,"kidn: "+(0,r.zw)(n.kind),1)])]))}};const w=b;var h=w;const f={class:"card"},z=(0,e._)("div",{class:"card-header"},"ChlidC",-1),k={class:"card-body"};var C={props:["product"],setup(n){return(a,t)=>((0,e.wg)(),(0,e.iD)("div",f,[z,(0,e._)("div",k,[(0,e._)("p",null,"no: "+(0,r.zw)(n.product.no),1),(0,e._)("p",null,"kidn: "+(0,r.zw)(n.product.kind),1)])]))}};const g=C;var S=g;const U={class:"card"},D=(0,e._)("div",{class:"card-header"},"Exam01Props",-1),y={class:"card-body"},E=(0,e._)("hr",null,null,-1),W=(0,e._)("hr",null,null,-1);var P={setup(n){const a=(0,d.qj)({no:1,kind:"bag"});function t(){a.no++,a.kind+="back"}return(n,r)=>{const l=(0,e.up)("ChildD");return(0,e.wg)(),(0,e.iD)("div",U,[D,(0,e._)("div",y,[(0,e.Wm)(p,{prop1:"value1",prop2:"value2",productNo:"1","product-kind":"bag",productPrice:"1000"}),E,(0,e.Wm)(h,{no:(0,d.SU)(a).no,kind:(0,d.SU)(a).kind},null,8,["no","kind"]),(0,e.Wm)(S,{product:(0,d.SU)(a)},null,8,["product"]),(0,e._)("button",{onClick:t,class:"btn btn-info btn-sm mt-2"},"데이터 변경"),W,(0,e.Wm)(l,{propA:"문자열",propB:1,"prop-C":2,"prop-d":"valuec","prop-e":3,"prop-f":{message:"안녕하세요"},"prop-g":["red","green","blue"],"prop-h":()=>"prop value","prop-i":-4},null,8,["prop-h"])])])}}};const N=P;var $=N},3910:function(n,a,t){t.r(a),t.d(a,{default:function(){return f}});var e=t(3396),d=t(7139),r=t(4870);const l={class:"card"},c=(0,e._)("div",{class:"card-header"},"Child",-1),o={class:"card-body"},u=(0,e._)("h6",null,"[자식 -> 부모]",-1),s=(0,e._)("h6",null,"[자식 -> 부모 -> 자식]",-1);var i={props:["counter"],emits:["child-event-2","increment-event","decrement-event"],setup(n,{emit:a}){const t=(0,r.iH)("value1"),i=(0,r.iH)(100);function p(){a("child-event-2",t.value,i.value)}function v(){a("increment-event")}function m(){a("decrement-event")}return(a,r)=>((0,e.wg)(),(0,e.iD)("div",l,[c,(0,e._)("div",o,[(0,e._)("div",null,[u,(0,e._)("button",{onClick:r[0]||(r[0]=n=>a.$emit("child-event-1",t.value)),class:"btn btn-info btn-sm mr-2"},"child-event-1 emit"),(0,e._)("button",{onClick:p,class:"btn btn-info btn-sm mr-2"},"child-event-2 emit")]),(0,e._)("div",null,[s,(0,e._)("button",{onClick:v,class:"btn btn-info btn-sm mr-2"},"increment-event emit"),(0,e._)("button",{onClick:m,class:"btn btn-info btn-sm mr-2"},"decrement-event emit"),(0,e._)("p",null,"Counter : "+(0,d.zw)(n.counter),1)])])]))}};const p=i;var v=p;const m={class:"card"},_=(0,e._)("div",{class:"card-header"},"Exam02EventEmit",-1),b={class:"card-body"};var w={setup(n){const a=(0,r.iH)(0);function t(n){console.log("data1 : ",n)}function d(n,a){console.log("data2 : ",a)}function l(){a.value--}return(n,r)=>((0,e.wg)(),(0,e.iD)("div",m,[_,(0,e._)("div",b,[(0,e.Wm)(v,{onChildEvent1:t,onChildEvent2:d,counter:a.value,onIncrementEvent:r[0]||(r[0]=n=>a.value++),onDecrementEvent:l},null,8,["counter"])])]))}};const h=w;var f=h},4957:function(n,a,t){t.r(a),t.d(a,{default:function(){return W}});var e=t(3396),d=t(4870),r=t(7139);const l={class:"card"},c=(0,e._)("div",{class:"card-header"},"ChildA [부모와 자식간의 관계가 아닌 포함 관계]",-1),o={class:"card-body"};var u={setup(n){const a=(0,e.f3)("message");function t(){a.data1.value+="1",a.data2.value.name1+="2",a.data2.value.name2+="3",a.data3.name3+="4",a.data3.name4+="5"}return(n,u)=>((0,e.wg)(),(0,e.iD)("div",l,[c,(0,e._)("div",o,[(0,e._)("div",null,[(0,e._)("p",null,"data1 : "+(0,r.zw)((0,d.SU)(a).data1.value),1),(0,e._)("p",null,"data2.name1 : "+(0,r.zw)((0,d.SU)(a).data2.value.name1),1),(0,e._)("p",null,"data2.name1 : "+(0,r.zw)((0,d.SU)(a).data2.value.name2),1),(0,e._)("p",null,"data3.name1 : "+(0,r.zw)((0,d.SU)(a).data3.name3),1),(0,e._)("p",null,"data3.name1 : "+(0,r.zw)((0,d.SU)(a).data3.name4),1)]),(0,e._)("button",{onClick:t,class:"mt-2 btn btn-info btn-sm"},"데이터 변경")])]))}};const s=u;var i=s;const p={class:"card"},v=(0,e._)("div",{class:"card-header"},"ChildC [부모와 자식간의 관계가 아닌 포함 관계]",-1),m={class:"card-body"};var _={setup(n){const a=(0,e.f3)("message");return(n,t)=>((0,e.wg)(),(0,e.iD)("div",p,[v,(0,e._)("div",m,[(0,e._)("div",null,[(0,e._)("p",null,"data1 : "+(0,r.zw)((0,d.SU)(a).data1.value),1),(0,e._)("p",null,"data2.name1 : "+(0,r.zw)((0,d.SU)(a).data2.value.name1),1),(0,e._)("p",null,"data2.name1 : "+(0,r.zw)((0,d.SU)(a).data2.value.name2),1),(0,e._)("p",null,"data3.name1 : "+(0,r.zw)((0,d.SU)(a).data3.name3),1),(0,e._)("p",null,"data3.name1 : "+(0,r.zw)((0,d.SU)(a).data3.name4),1)])])]))}};const b=_;var w=b;const h={class:"card"},f=(0,e._)("div",{class:"card-header"},"ChildB",-1),z={class:"card-body"};var k={setup(n){return(n,a)=>((0,e.wg)(),(0,e.iD)("div",h,[f,(0,e._)("div",z,[(0,e.Wm)(w)])]))}};const C=k;var g=C;const S={class:"card"},U=(0,e._)("div",{class:"card-header"},"Exam03ProvideInject",-1),D={class:"card-body"};var y={setup(n){const a=(0,d.iH)("parent-value1"),t=(0,d.iH)({name1:"parent-value2",name2:"parent-value3"}),r=(0,d.qj)({name3:"parent-value4",name4:"parent-value5"});return(0,e.JJ)("message",{data1:a,data2:t,data3:r}),(n,a)=>((0,e.wg)(),(0,e.iD)("div",S,[U,(0,e._)("div",D,[(0,e.Wm)(i,{class:"mb-2"}),(0,e.Wm)((0,d.SU)(g))])]))}};const E=y;var W=E}}]);
//# sourceMappingURL=menu04.825edc94.js.map