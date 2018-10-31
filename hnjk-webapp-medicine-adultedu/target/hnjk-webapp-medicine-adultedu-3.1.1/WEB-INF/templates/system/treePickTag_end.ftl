</div>
				</td>
				<td align="center" style="width:80px;padding-top:50;">
					<button id="${id}PickAll">
						全选&gt;&gt;
					</button>
					<br />
					<br />
					<button id="${id}Delselected">
						&lt;删除&lt;
					</button>
					<br />
					<br />
					<button id="${id}DelAll">
						&lt;&lt;全删
					</button>
					<br />
					<br />
					<br />
					<br />

				</td>
				<td align="left" style="padding-top:0;">
					<b>&nbsp;已选：</b>
					<br />
					<select id="${id}SelectArea" name="${id}SelectArea" multiple="multiple"
						style="width:200px;height:278px;overflow:auto;border:hidden;background-color: #fdfdfd;display: inline;"></select>
				</td>
			</tr>
			<tr>
				<td colspan="2"
					style="color:red;padding-top: 5px;padding-bottom: 5px;">
					&nbsp;操作提示：<span id="${id}MessageOut">无</span>
				</td>
				<td align="right" style="padding-top: 5px;padding-bottom: 5px;">
					<button id="${id}DoPick">
						&gt;确定&lt;
					</button>
					&#12288;&#12288;
					<button onclick="window.close();">
						&lt;取消&gt;
					</button>
				</td>
			</tr>
		</table>