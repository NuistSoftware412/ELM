single = load('exp2_result1.data');
node4 = load('exp2_result2.data');
node6 = load('exp2_result3.data');
node8 = load('exp2_result4.data');
size=load('exp2_result0.data');

plot(size/100,node4/189.094977,'k-+',size/100,node6/125.7346167,'r-o',size/100,node8/128.153694,'b-*');
legend('4 nodes','6 nodes','8 nodes');
xlabel('Sizeup of Training ELMs');
ylabel('Sizeup');
title('Sizeup of Multiple ELMs Training on Hadoop Cluster');