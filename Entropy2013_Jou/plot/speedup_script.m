function speedup_script
data;
figure(1);
plot(x,speedup4(:,1),'-k*',x,speedup6(:,1),'-r+',x,speedup8(:,1),'-bo');
xlabel('Number of Samples (x1000)');
ylabel('speedup');
title('Speedup of Training with 5 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
hold on;

figure(2);
plot(x,speedup4(:,2),'-k*',x,speedup6(:,2),'-r+',x,speedup8(:,2),'-bo');
xlabel('Number of Samples (x1000)');
ylabel('speedup');
title('Speedup of Training with 7 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
hold on;

figure(3);
plot(x,speedup4(:,3),'-k*',x,speedup6(:,3),'-r+',x,speedup8(:,3),'-bo');
xlabel('Number of Samples (x1000)');
ylabel('speedup');
title('Speedup of Training with 9 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');

figure(4);
plot(x,speedup4(:,4),'-k*',x,speedup6(:,4),'-r+',x,speedup8(:,4),'-bo');
xlabel('Number of Samples (x1000)');
ylabel('speedup');
title('Speedup of Training with 11 Iteractions on Hadoop Cluster');
legend('4 nodes','6 nodes','8 nodes');
