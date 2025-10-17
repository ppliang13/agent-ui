Vue.component('step-sql', {
    props: ['step', 'env'],
    data() {
        return {
            dsOptions: []
        };
    },
    watch: {
        env(newEnv) {
            if (newEnv) {
                Vue.set(this.step, 'env', newEnv);  // 保证步骤的 env 跟全局同步
                this.loadDsOptions(newEnv);
            }
        }
    },
    methods: {
        loadDsOptions(env) {
            axios.get('/ds/list', { params: { env } })
                .then(res => {
                    this.dsOptions = res.data || [];
                    if (this.step.ds && !this.dsOptions.includes(this.step.ds)) {
                        Vue.set(this.step, 'ds', '');
                    }
                });
        }
    },
    template: `
    <el-form label-width="120px">
        <el-form-item label="数据源">
            <el-select v-model="step.ds" placeholder="请选择数据源">
                <el-option v-for="ds in dsOptions" :key="ds" :label="ds" :value="ds"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item label="SQL语句">
            <textarea class="code-editor" v-model="step.sql"
                      placeholder="请输入 SQL 语句，例如：select * from account where cstid=\'\${input#cstid}\'">
            </textarea>
        </el-form-item>
    </el-form>
    `
});
