Vue.component('step-common', {
    props: ['step'],
    template: `
    <el-form label-width="120px">
        <el-row :gutter="20">
            <!-- 步骤代码 -->
            <el-col :span="10">
                <el-form-item label="步骤代码">
                    <el-input v-model="step.code" placeholder="如：step1"></el-input>
                </el-form-item>
            </el-col>

            <!-- 步骤名称 -->
            <el-col :span="10">
                <el-form-item label="步骤名称">
                    <el-input v-model="step.name" placeholder="请输入步骤名称"></el-input>
                </el-form-item>
            </el-col>
        </el-row>

        <el-form-item label="步骤类型">
            <el-select v-model="step.type" placeholder="请选择步骤类型">
                <el-option label="SQL 执行" value="sql"></el-option>
                <el-option label="请求执行" value="request"></el-option>
            </el-select>
        </el-form-item>
    </el-form>
    `
});
