new Vue({
    el:'#app',
    data:{
        form:{
            processCode:'',
            processName:'',
            processDesc:'',
            script:'',
            steps:[]
        }
    },
    methods:{
        addStep(){
            this.form.steps.push({
                stepId:this.uuid(),
                type:'',
                name:'',
                desc:'',
                env:'',
                sql:'',
                requestBody:''
            });
        },
        removeStep(index){
            this.form.steps.splice(index,1);
        },
        getStepComponent(type){
            switch(type){
                case 'sql': return 'step-sql';
                case 'request': return 'step-request';
                default: return null;
            }
        },
        uuid(){
            return 'xxxx-xxxx-4xxx-yxxx'.replace(/[xy]/g,c=>{
                const r = Math.random()*16|0;
                const v = c==='x'?r:(r&0x3|0x8);
                return v.toString(16);
            });
        },
        saveProcess(){
            axios.post('/process/save', this.form)
                .then(res=>{
                    this.$message.success('保存成功');
                }).catch(err=>{
                this.$message.error('保存失败');
            });
        }
    }
});
